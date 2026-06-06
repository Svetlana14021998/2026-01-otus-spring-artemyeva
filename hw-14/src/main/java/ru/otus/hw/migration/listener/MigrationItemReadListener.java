package ru.otus.hw.migration.listener;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.migration.table.MigrationContext;
import ru.otus.hw.models.h2.H2Entity;

@Component
@RequiredArgsConstructor
public class MigrationItemReadListener <T extends H2Entity> implements ItemReadListener<T> {

    private final MigrationContext migrationContext;

    @Override
    public void beforeRead() {
        if (migrationContext.getCurrentTable() == null) {
            throw new IllegalStateException("Current table not set in MigrationContext");
        }
    }

    @Override
    public void afterRead(@Nonnull T entity) {
        migrationContext.addSourceId(entity.getId());
    }
}
