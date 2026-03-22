package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent
public class TestCommand {

    private final TestRunnerService testRunnerService;

    private final AppProperties properties;

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    @ShellMethod(value = "Run testing", key = "rt")
    public String runTests() {
        testRunnerService.run();
        String message = messageSource.getMessage("End.Testing", null, localeConfig.getLocale());
        return getStringWithColor(message);
    }

    @ShellMethod(value = "Change language: ru - Russian, en - English", key = "cl")
    public String setLocale(String language) {
        switch (language.toUpperCase()) {
            case "RU":
                properties.setLocale("ru-RU");
                break;
            case "EN":
                properties.setLocale("en-EN");
                break;
        }
        String message = messageSource.getMessage("Current.language", null, localeConfig.getLocale());
        return getStringWithColor(message);
    }

    private String getStringWithColor(String string) {
        String green = "\u001B[32m";
        String reset = "\u001B[0m";
        return green + string + reset;
    }
}
