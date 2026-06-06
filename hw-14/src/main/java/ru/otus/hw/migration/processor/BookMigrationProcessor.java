package ru.otus.hw.migration.processor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converter.Book2BookDocumentConverter;
import ru.otus.hw.migration.table.MigrationTableService;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.repositories.mongo.AuthorRepositoryMongo;
import ru.otus.hw.repositories.mongo.GenreRepositoryMongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.hw.migration.table.MigrationTable.BOOKS;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookMigrationProcessor implements ItemProcessor<Book, BookDocument> {

    private final Book2BookDocumentConverter converter;

    private final MigrationTableService migrationTableService;

    private final AuthorMigrationProcessor authorMigrationProcessor;

    private final GenreMigrationProcessor genreMigrationProcessor;

    private final AuthorRepositoryMongo authorRepository;

    private final GenreRepositoryMongo genreRepository;

    @Getter
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    private Map<Long, AuthorDocument> authorCache = new ConcurrentHashMap<>();

    private Map<Long, GenreDocument> genreCache = new ConcurrentHashMap<>();

    @BeforeStep
    public void beforeStep() {
        log.info("Loading existing books mappings into cache");

        Map<Long, String> existingBookMappings = migrationTableService.getAllMappingsAsMap(BOOKS);
        cache.putAll(existingBookMappings);

        log.info("Loaded {} existing book mappings into cache", cache.size());

        authorCache = resolveAuthorDocuments();
        genreCache = resolveGenreDocuments();
    }

    @Override
    public BookDocument process(Book book) {
        AuthorDocument authorDocument = authorCache.get(book.getAuthor().getId());
        List<Long> genreIds = book.getGenres().stream().map(Genre::getId).toList();
        List<GenreDocument> genreDocuments = genreIds.stream().map(x -> genreCache.get(x)).toList();
        String targetId = cache.get(book.getId());

        if (targetId == null) {
            return converter.convert(book, null, authorDocument, genreDocuments);
        }
        return converter.convert(book, targetId, authorDocument, genreDocuments);
    }

    public void updateCacheBatch(Map<Long, String> newMappings) {
        cache.putAll(newMappings);
        log.info("Batch updated cache with {} new mappings", newMappings.size());
    }

    public Map<Long, AuthorDocument> resolveAuthorDocuments() {
        Map<Long, String> authorsMapping = authorMigrationProcessor.getCache();
        return buildAuthorCache(authorsMapping);
    }

    public Map<Long, GenreDocument> resolveGenreDocuments() {
        Map<Long, String> genreMapping = genreMigrationProcessor.getCache();
        return buildGenreCache(genreMapping);
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