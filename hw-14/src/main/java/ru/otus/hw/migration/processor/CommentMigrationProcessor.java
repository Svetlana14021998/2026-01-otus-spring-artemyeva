package ru.otus.hw.migration.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converter.Comment2CommentDocumentConverter;
import ru.otus.hw.migration.table.MigrationTableService;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.repositories.mongo.BookRepositoryMongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.hw.migration.table.MigrationTable.COMMENTS;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentMigrationProcessor implements ItemProcessor<Comment, CommentDocument> {

    private final Comment2CommentDocumentConverter converter;

    private final MigrationTableService migrationTableService;

    private final BookMigrationProcessor bookMigrationProcessor;

    private final BookRepositoryMongo bookRepository;

    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    private Map<Long, BookDocument> bookCache = new ConcurrentHashMap<>();

    @BeforeStep
    public void beforeStep() {
        log.info("Loading existing comments mappings into cache");

        Map<Long, String> existingBookMappings = migrationTableService.getAllMappingsAsMap(COMMENTS);
        cache.putAll(existingBookMappings);

        log.info("Loaded {} existing comments mappings into cache", cache.size());

        bookCache = resolveBookDocuments();
    }

    @Override
    public CommentDocument process(Comment comment) {
        BookDocument bookDocument = bookCache.get(comment.getBook().getId());
        String targetId = cache.get(comment.getId());

        if (targetId == null) {
            return converter.convert(comment, null, bookDocument);
        }
        return converter.convert(comment, targetId, bookDocument);
    }

    public void updateCacheBatch(Map<Long, String> newMappings) {
        cache.putAll(newMappings);
        log.info("Batch updated cache with {} new mappings", newMappings.size());
    }

    public Map<Long, BookDocument> resolveBookDocuments() {
        Map<Long, String> bookMapping = bookMigrationProcessor.getCache();
        return buildBookCache(bookMapping);
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
}