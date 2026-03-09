package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionIncorrectDataException;

import java.util.ArrayList;
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
        var questionsForPrint = mapQuestionsForPrint(questions);
        questionsForPrint
            .forEach(ioService::printLine);
    }

    private List<String> mapQuestionsForPrint(List<Question> questions) {
        var stringQuestions = new ArrayList<String>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            var stringQuestion = convertQuestionToString(question, i);
            stringQuestions.add(stringQuestion);
        }
        return stringQuestions;
    }

    private String convertQuestionToString(Question question, int i) {
        if (question.answers() == null) {
            throw new QuestionIncorrectDataException("Not answers for question " + (i + 1));
        }
        var stringBuilder = new StringBuilder();
        stringBuilder.append(i + 1).append(".").append(question.text()).append("\n");
        for (int j = 0; j < question.answers().size(); j++) {
            Answer answer = question.answers().get(j);
            stringBuilder.append(j + 1).append(")").append(answer.text()).append("\n");
        }
        return stringBuilder.toString();
    }
}