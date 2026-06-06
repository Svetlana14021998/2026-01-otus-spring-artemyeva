package ru.otus.hw.migration.processor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converter.Genre2GenreDocumentConverter;
import ru.otus.hw.migration.table.MigrationTableService;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.hw.migration.table.MigrationTable.GENRES;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreMigrationProcessor implements ItemProcessor<Genre, GenreDocument> {

    private final Genre2GenreDocumentConverter converter;

    private final MigrationTableService migrationTableService;

    @Getter
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    @BeforeStep
    public void beforeStep() {
        log.info("Loading existing genres mappings into cache");

        Map<Long, String> existingMappings = migrationTableService.getAllMappingsAsMap(GENRES);
        cache.putAll(existingMappings);

        log.info("Loaded {} existing genres mappings into cache", cache.size());
    }

    @Override
    public GenreDocument process(Genre genre) {
        String targetId = cache.get(genre.getId());
        if (targetId == null) {
            return converter.convert(genre, null);
        }
        return converter.convert(genre, targetId);
    }

    public void updateCacheBatch(Map<Long, String> newMappings) {
        cache.putAll(newMappings);
        log.info("Batch updated cache with {} new mappings", newMappings.size());
    }
}
