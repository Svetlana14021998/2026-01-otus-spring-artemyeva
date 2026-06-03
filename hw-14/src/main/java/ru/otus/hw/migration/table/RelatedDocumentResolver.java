package ru.otus.hw.migration.table;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.repositories.mongo.AuthorRepositoryMongo;
import ru.otus.hw.repositories.mongo.BookRepositoryMongo;
import ru.otus.hw.repositories.mongo.GenreRepositoryMongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.otus.hw.migration.table.MigrationTable.AUTHORS;
import static ru.otus.hw.migration.table.MigrationTable.BOOKS;
import static ru.otus.hw.migration.table.MigrationTable.GENRES;

@Component
@RequiredArgsConstructor
@Slf4j
public class RelatedDocumentResolver {

    private final MigrationTableService migrationTableService;

    private final AuthorRepositoryMongo authorRepository;

    private final GenreRepositoryMongo genreRepository;

    private final BookRepositoryMongo bookRepository;

    public Map<Long, BookDocument> resolveBookDocuments() {
        Map<Long, String> bookMapping = migrationTableService.getAllMappingsAsMap(BOOKS);
        return buildBookCache(bookMapping);
    }

    public Map<Long, AuthorDocument> resolveAuthorDocuments() {
        Map<Long, String> authorsMapping = migrationTableService.getAllMappingsAsMap(AUTHORS);
        return buildAuthorCache(authorsMapping);
    }

    public Map<Long, GenreDocument> resolveGenreDocuments() {
        Map<Long, String> genreMapping = migrationTableService.getAllMappingsAsMap(GENRES);
        return buildGenreCache(genreMapping);
    }

    private Map<Long, BookDocument> buildBookCache(Map<Long, String> bookMappings) {
        List<BookDocument> allBooks = bookRepository.findAll();

        Map<String, Long> targetToSource = new HashMap<>();
        for (Map.Entry<Long, String> entry : bookMappings.entrySet()) {
            targetToSource.put(entry.getValue(), entry.getKey());
        }

        Map<Long, BookDocument> cache = new HashMap<>();
        for (BookDocument book : allBooks) {
            Long sourceId = targetToSource.get(book.getId());
            if (sourceId != null) {
                cache.put(sourceId, book);
            } else {
                log.warn("Book with targetId {} has no sourceId mapping", book.getId());
            }
        }

        log.info("Build book cache with {} entries", cache.size());
        return cache;
    }

    private Map<Long, AuthorDocument> buildAuthorCache(Map<Long, String> authorMappings) {
        List<AuthorDocument> allAuthors = authorRepository.findAll();

        Map<String, Long> targetToSource = new HashMap<>();
        for (Map.Entry<Long, String> entry : authorMappings.entrySet()) {
            targetToSource.put(entry.getValue(), entry.getKey());
        }

        Map<Long, AuthorDocument> cache = new HashMap<>();
        for (AuthorDocument author : allAuthors) {
            Long sourceId = targetToSource.get(author.getId());
            if (sourceId != null) {
                cache.put(sourceId, author);
            } else {
                log.warn("Author with targetId {} has no sourceId mapping", author.getId());
            }
        }

        log.info("Build author cache with {} entries", cache.size());
        return cache;
    }

    private Map<Long, GenreDocument> buildGenreCache(Map<Long, String> genreMappings) {
        List<GenreDocument> allGenres = genreRepository.findAll();

        Map<String, Long> targetToSource = new HashMap<>();
        for (Map.Entry<Long, String> entry : genreMappings.entrySet()) {
            targetToSource.put(entry.getValue(), entry.getKey());
        }

        Map<Long, GenreDocument> cache = new HashMap<>();
        for (GenreDocument genre : allGenres) {
            Long sourceId = targetToSource.get(genre.getId());
            if (sourceId != null) {
                cache.put(sourceId, genre);
            } else {
                log.warn("Genre with targetId {} has no sourceId mapping", genre.getId());
            }
        }

        log.info("Build genre cache with {} entries", cache.size());
        return cache;
    }
}
