package ru.otus.hw.migration.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converter.Author2AuthorDocumentConverter;
import ru.otus.hw.migration.table.MigrationTableService;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.mongo.AuthorDocument;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.hw.migration.table.MigrationTable.AUTHORS;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorMigrationProcessor implements ItemProcessor<Author, AuthorDocument> {

    private final Author2AuthorDocumentConverter converter;

    private final MigrationTableService migrationTableService;

    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    @BeforeStep
    public void beforeStep() {
        log.info("Loading existing author mappings into cache");

        Map<Long, String> existingMappings = migrationTableService.getAllMappingsAsMap(AUTHORS);
        cache.putAll(existingMappings);

        log.info("Loaded {} existing author mappings into cache", cache.size());
    }

    @Override
    public AuthorDocument process(Author author) {
        String targetId = cache.get(author.getId());
        if (targetId == null) {
            return converter.convert(author, null);
        }
        return converter.convert(author, targetId);
    }

    public void updateCacheBatch(Map<Long, String> newMappings) {
        cache.putAll(newMappings);
        log.info("Batch updated cache with {} new mappings", newMappings.size());
    }
}
