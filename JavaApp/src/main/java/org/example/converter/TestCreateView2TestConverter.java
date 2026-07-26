package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Test;
import org.example.view.test.create.QuestionCreateView;
import org.example.view.test.create.TestCreateView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestCreateView2TestConverter {

    public Test convert(TestCreateView view) {
        Test test = Test.builder()
            .title(view.getTitle())
            .needCorrectAnswerCount(view.getNeedCorrectAnswerCount())
            .build();
        List<Question> questions = convertQuestions(view.getQuestions(), test);
        test.setQuestions(questions);
        return test;
    }

    List<Question> convertQuestions(List<QuestionCreateView> questions, Test test) {
        List<Question> result = new ArrayList<>();
        for (QuestionCreateView questionCreateView : questions) {
            Question question = Question.builder()
                .text(questionCreateView.getText())
                .questionType(questionCreateView.getQuestionType())
                .test(test)
                .build();
            List<Answer> answers = convertAnswers(question, questionCreateView);
            question.setAnswers(answers);
            result.add(question);
        }
        return result;
    }

    List<Answer> convertAnswers(Question question, QuestionCreateView q) {
        return q.getAnswers().stream()
            .map(a -> Answer.builder()
                .question(question)
                .text(a.getText())
                .isCorrect(a.getIsCorrect())
                .build())
            .toList();
    }
}
