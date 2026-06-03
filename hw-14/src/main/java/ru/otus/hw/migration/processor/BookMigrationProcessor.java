package ru.otus.hw.migration.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converter.Book2BookDocumentConverter;
import ru.otus.hw.migration.table.MigrationTableService;
import ru.otus.hw.migration.table.RelatedDocumentResolver;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.GenreDocument;

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

    private final RelatedDocumentResolver resolver;

    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    private Map<Long, AuthorDocument> authorCache = new ConcurrentHashMap<>();

    private Map<Long, GenreDocument> genreCache = new ConcurrentHashMap<>();

    @BeforeStep
    public void beforeStep() {
        log.info("Loading existing books mappings into cache");

        Map<Long, String> existingBookMappings = migrationTableService.getAllMappingsAsMap(BOOKS);
        cache.putAll(existingBookMappings);

        log.info("Loaded {} existing book mappings into cache", cache.size());

        authorCache = resolver.resolveAuthorDocuments();
        genreCache = resolver.resolveGenreDocuments();
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
}