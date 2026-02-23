package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        printQuestionsWithAnswers(questions);
    }

    private void printQuestionsWithAnswers(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (question.answers() == null) {
                throw new QuestionIncorrectDataException("Not answers for question " + (i + 1));
            }
            ioService.printLine(i + 1 + "." + question.text());
            for (int j = 0; j < question.answers().size(); j++) {
                Answer answer = question.answers().get(j);
                ioService.printLine(j + 1 + ")" + answer.text());
            }
            ioService.printLine("");
        }
    }
}
