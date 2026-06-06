package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.util.Properties;

@RequiredArgsConstructor
@ShellComponent
public class Commands {

    private static final String JOB_NAME = "migrateJob";

    private final JobOperator jobOperator;

    @ShellMethod(value = "Migrate from h2 to MongoDB", key = "h2m")
    public void migrateFromH2ToMongoDB() throws Exception {
        Properties properties = new Properties();
        properties.put("currentDateTime", LocalDateTime.now().toString());

        jobOperator.start(JOB_NAME, properties);
    }
}
