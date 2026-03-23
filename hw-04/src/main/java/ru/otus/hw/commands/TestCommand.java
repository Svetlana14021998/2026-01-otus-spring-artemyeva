package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent
public class TestCommand {

    private final TestRunnerService testRunnerService;

    private final AppProperties properties;

    private final MessageSource messageSource;

    @ShellMethod(value = "Run testing", key = "rt")
    public String runTests() {
        testRunnerService.run();
        String message = messageSource.getMessage("End.Testing", null, properties.getLocale());
        return getStringWithColor(message);
    }

    @ShellMethod(value = "Change language: ru - Russian, en - English", key = "cl")
    public String setLocale(String language) {
        properties.setLocaleByCountry(language);
        String message = messageSource.getMessage("Current.language", null, properties.getLocale());
        return getStringWithColor(message);
    }

    private String getStringWithColor(String string) {
        String green = "\u001B[32m";
        String reset = "\u001B[0m";
        return green + string + reset;
    }
}
