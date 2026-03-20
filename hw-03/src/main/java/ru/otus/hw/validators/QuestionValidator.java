package ru.otus.hw.validators;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
public class QuestionValidator {
    public boolean isQuestionsHasAnswers(List<Question> questions) {
        return questions.stream()
            .noneMatch(question -> question.answers() == null);
    }
}
