package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Test;
import org.example.view.test.main.AnswerView;
import org.example.view.test.main.QuestionView;
import org.example.view.test.main.TestView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Test2TestViewConverter {

    public TestView convert(Test test) {
        return TestView.builder()
            .id(test.getId())
            .title(test.getTitle())
            .courseId(test.getCourse().getId())
            .needCorrectAnswerCount(test.getNeedCorrectAnswerCount())
            .questions(test.getQuestions().stream()
                .map(this::convertQuestion)
                .toList())
            .build();
    }

    public QuestionView convertQuestion(Question question) {
        return QuestionView.builder()
            .id(question.getId())
            .questionType(question.getQuestionType())
            .text(question.getText())
            .answers(question.getAnswers().stream()
                .map(this::convertAnswer)
                .toList())
            .build();
    }

    private AnswerView convertAnswer(Answer answer) {
        return AnswerView.builder()
            .id(answer.getId())
            .text(answer.getText())
            .isCorrect(answer.isCorrect())
            .build();
    }
}
