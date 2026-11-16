package org.example.converter;

import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Test;
import org.example.dto.test.main.AnswerDto;
import org.example.dto.test.main.QuestionDto;
import org.example.dto.test.main.TestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDto2TestConverter {

    public Test convert(Test test, TestDto dto) {
        test.setTitle(dto.getTitle());
        test.setNeedCorrectAnswerCount(dto.getNeedCorrectAnswerCount());
        List<Question> questions = dto.getQuestions().stream()
            .map(q -> convertQuestion(q, test))
            .toList();
        test.getQuestions().forEach(x -> x.getAnswers().clear());
        test.getQuestions().clear();
        test.getQuestions().addAll(questions);
        return test;
    }

    private Question convertQuestion(QuestionDto dto, Test test) {
        Question question = Question.builder()
            .id(dto.getId())
            .questionType(dto.getQuestionType())
            .text(dto.getText())
            .test(test)
            .build();
        List<Answer> answers = dto.getAnswers().stream()
            .map(a -> convertAnswer(a, question))
            .toList();
        question.setAnswers(answers);
        return question;
    }

    private Answer convertAnswer(AnswerDto dto, Question question) {
        return Answer.builder()
            .id(dto.getId())
            .isCorrect(dto.getIsCorrect())
            .text(dto.getText())
            .question(question)
            .build();
    }
}
