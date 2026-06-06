package ru.otus.hw.migration.listener;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;
import ru.otus.hw.migration.processor.AuthorMigrationProcessor;
import ru.otus.hw.migration.processor.BookMigrationProcessor;
import ru.otus.hw.migration.processor.CommentMigrationProcessor;
import ru.otus.hw.migration.processor.GenreMigrationProcessor;
import ru.otus.hw.migration.table.MigrationContext;
import ru.otus.hw.migration.table.MigrationTable;
import ru.otus.hw.migration.table.MigrationTableService;
import ru.otus.hw.models.mongo.MongoDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MigrationItemWriteListener<T extends MongoDocument> implements ItemWriteListener<T> {

    private final MigrationContext context;

    private final MigrationTableService migrationTableService;

    private final AuthorMigrationProcessor authorProcessor;

    private final GenreMigrationProcessor genreProcessor;

    private final BookMigrationProcessor bookProcessor;

    private final CommentMigrationProcessor commentProcessor;

    @Override
    public void afterWrite(@Nonnull Chunk<? extends T> documents) {
        List<Long> sourceIds = context.getAndClearSourceIds();
        List<String> targetIds = documents.getItems().stream()
            .map(MongoDocument::getId)
            .collect(Collectors.toList());

        migrationTableService.saveMapping(context.getCurrentTable(), sourceIds, targetIds);
        MigrationTable table = context.getCurrentTable();

        updateProcessorCache(table, sourceIds, targetIds);
    }

    private void updateProcessorCache(MigrationTable table, List<Long> sourceIds, List<String> targetIds) {
        Map<Long, String> batchMapping = new HashMap<>();
        for (int i = 0; i < sourceIds.size(); i++) {
            batchMapping.put(sourceIds.get(i), targetIds.get(i));
        }

        switch (table) {
            case AUTHORS -> {
                authorProcessor.updateCacheBatch(batchMapping);
            }
            case GENRES -> {
                genreProcessor.updateCacheBatch(batchMapping);
            }
            case BOOKS -> {
                bookProcessor.updateCacheBatch(batchMapping);
            }
            case COMMENTS -> {
                commentProcessor.updateCacheBatch(batchMapping);
            }
        }
    }
}
