package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;
import ru.otus.hw.validators.QuestionValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionValidator questionValidator;

    @Override
    public TestResult executeTestFor(Student student) {
        var questions = questionDao.findAll();
        if (!questionValidator.isQuestionsHasAnswers(questions)) {
            throw new QuestionIncorrectDataException("Not answer for question");
        }
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        return passTheTest(student, questions);
    }

    private TestResult passTheTest(Student student, List<Question> questions) {
        var testResult = new TestResult(student);
        for (int i = 0; i < questions.size(); i++) {
            var stringQuestion = convertQuestionToString(questions.get(i), i);
            ioService.printLine(stringQuestion);
            int answerNumber = ioService.readIntForRangeWithPromptLocalized(1, questions.get(i).answers().size(),
                "TestService.answer.prompt", "TestService.incorrect.answer.number");
            var isAnswerValid = questions.get(i).answers().get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(questions.get(i), isAnswerValid);
        }
        return testResult;
    }

    private String convertQuestionToString(Question question, int i) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(i + 1).append(".").append(question.text()).append("\n");
        for (int j = 0; j < question.answers().size(); j++) {
            Answer answer = question.answers().get(j);
            stringBuilder.append(j + 1).append(")").append(answer.text()).append("\n");
        }
        return stringBuilder.toString();
    }
}
