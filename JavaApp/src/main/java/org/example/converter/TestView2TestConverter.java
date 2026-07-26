package org.example.converter;

import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Test;
import org.example.view.test.main.AnswerView;
import org.example.view.test.main.QuestionView;
import org.example.view.test.main.TestView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestView2TestConverter {

    public Test convert(Test test, TestView view) {
        test.setTitle(view.getTitle());
        test.setNeedCorrectAnswerCount(view.getNeedCorrectAnswerCount());
        List<Question> questions = view.getQuestions().stream()
            .map(q -> convertQuestion(q, test))
            .toList();
        test.getQuestions().forEach(x -> x.getAnswers().clear());
        test.getQuestions().clear();
        test.getQuestions().addAll(questions);
        return test;
    }

    private Question convertQuestion(QuestionView view, Test test) {
        Question question = Question.builder()
            .id(view.getId())
            .questionType(view.getQuestionType())
            .text(view.getText())
            .test(test)
            .build();
        List<Answer> answers = view.getAnswers().stream()
            .map(a -> convertAnswer(a, question))
            .toList();
        question.setAnswers(answers);
        return question;
    }

    private Answer convertAnswer(AnswerView view, Question question) {
        return Answer.builder()
            .id(view.getId())
            .isCorrect(view.getIsCorrect())
            .text(view.getText())
            .question(question)
            .build();
    }
}
