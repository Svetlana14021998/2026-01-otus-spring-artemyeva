package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Test;
import org.example.dto.test.create.QuestionCreateDto;
import org.example.dto.test.create.TestCreateDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestCreateDto2TestConverter {

    public Test convert(TestCreateDto dto) {
        Test test = Test.builder()
            .title(dto.getTitle())
            .needCorrectAnswerCount(dto.getNeedCorrectAnswerCount())
            .build();
        List<Question> questions = convertQuestions(dto.getQuestions(), test);
        test.setQuestions(questions);
        return test;
    }

    List<Question> convertQuestions(List<QuestionCreateDto> questions, Test test) {
        List<Question> result = new ArrayList<>();
        for (QuestionCreateDto questionCreateDto : questions) {
            Question question = Question.builder()
                .text(questionCreateDto.getText())
                .questionType(questionCreateDto.getQuestionType())
                .test(test)
                .build();
            List<Answer> answers = convertAnswers(question, questionCreateDto);
            question.setAnswers(answers);
            result.add(question);
        }
        return result;
    }

    List<Answer> convertAnswers(Question question, QuestionCreateDto q) {
        return q.getAnswers().stream()
            .map(a -> Answer.builder()
                .question(question)
                .text(a.getText())
                .isCorrect(a.getIsCorrect())
                .build())
            .toList();
    }
}
