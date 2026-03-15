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

    private final LocalizedIOService ioService;

    @Override
    public void run() {
        var student = studentService.determineCurrentStudent();
        try {
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            ioService.printLineLocalized("Exception.can.not.read.questions");
        } catch (QuestionIncorrectDataException e) {
            ioService.printLineLocalized("Exception.incorrect.question.data");
        } catch (RuntimeException e) {
            if (e.getCause() instanceof QuestionIncorrectDataException) {
                ioService.printLineLocalized("Exception.incorrect.question.data");
            } else {
                ioService.printLineLocalized("Exception.other");
            }
        }
    }
}
