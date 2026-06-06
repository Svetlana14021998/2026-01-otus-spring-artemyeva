package ru.otus.hw.migration.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import ru.otus.hw.migration.table.MigrationContext;
import ru.otus.hw.migration.table.MigrationTable;

@RequiredArgsConstructor
@Slf4j
public class MigrationStepExecutionListener implements StepExecutionListener {

    private final MigrationContext context;

    private final MigrationTable table;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        context.setCurrentTable(table);
        log.info("Starting {} migration for: {}", stepExecution.getStepName(), table.getEntityName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Completed {} migration. Read: {}, Write: {}",
            stepExecution.getStepName(),
            stepExecution.getReadCount(),
            stepExecution.getWriteCount());
        return ExitStatus.COMPLETED;
    }
}
