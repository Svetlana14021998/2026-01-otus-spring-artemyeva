package ru.otus.hw.migration.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.migration.table.MigrationContext;
import ru.otus.hw.migration.table.MigrationTable;

@Component
@RequiredArgsConstructor
@Slf4j
public class MigrationStepExecutionListener implements StepExecutionListener {

    private final MigrationContext context;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        MigrationTable table = extractTableFromStepName(stepName);
        context.setCurrentTable(table);
        log.info("Starting {} migration for: {}", stepName, table.getEntityName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Completed {} migration. Read: {}, Write: {}",
            stepExecution.getStepName(),
            stepExecution.getReadCount(),
            stepExecution.getWriteCount());
        return ExitStatus.COMPLETED;
    }

    private MigrationTable extractTableFromStepName(String stepName) {
        return switch (stepName) {
            case "migrateAuthorStep" -> MigrationTable.AUTHORS;
            case "migrateGenreStep" -> MigrationTable.GENRES;
            case "migrateBookStep" -> MigrationTable.BOOKS;
            case "migrateCommentStep" -> MigrationTable.COMMENTS;
            default -> throw new IllegalArgumentException("Unknown step: " + stepName);
        };
    }
}
