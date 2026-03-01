package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final IOService ioService;

    @Override
    public void run() {
        try {
            testService.executeTest();
        } catch (QuestionReadException e) {
            ioService.printLine("Can`t read questions");
        } catch (QuestionIncorrectDataException e) {
            ioService.printLine("Incorrect questions data");
        } catch (RuntimeException e) {
            if (e.getCause() instanceof QuestionIncorrectDataException) {
                ioService.printLine("Incorrect questions data");
            } else {
                ioService.printLine("Other error. Write to support");
            }
        }
    }
}
