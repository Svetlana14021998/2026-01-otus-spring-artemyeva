package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final IOService ioService;

    @Override
    public void run() {
        var student = studentService.determineCurrentStudent();
        try {
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
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
