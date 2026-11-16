package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Test;
import org.example.dto.test.main.AnswerDto;
import org.example.dto.test.main.QuestionDto;
import org.example.dto.test.main.TestDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Test2TestDtoConverter {

    public TestDto convert(Test test) {
        return TestDto.builder()
            .id(test.getId())
            .title(test.getTitle())
            .courseId(test.getCourse().getId())
            .needCorrectAnswerCount(test.getNeedCorrectAnswerCount())
            .questions(test.getQuestions().stream()
                .map(this::convertQuestion)
                .toList())
            .build();
    }

    public QuestionDto convertQuestion(Question question) {
        return QuestionDto.builder()
            .id(question.getId())
            .questionType(question.getQuestionType())
            .text(question.getText())
            .answers(question.getAnswers().stream()
                .map(this::convertAnswer)
                .toList())
            .build();
    }

    private AnswerDto convertAnswer(Answer answer) {
        return AnswerDto.builder()
            .id(answer.getId())
            .text(answer.getText())
            .isCorrect(answer.isCorrect())
            .build();
    }
}
