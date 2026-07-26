package org.example.validator;

import org.example.enums.QuestionType;
import org.example.view.test.base.AbstractAnswerView;
import org.example.view.test.base.AbstractQuestionView;
import org.example.view.test.base.AbstractTestView;
import org.example.view.test.create.TestCreateView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestValidator {

    public <A extends AbstractAnswerView, Q extends AbstractQuestionView<A>, T extends AbstractTestView<Q>> Map<String, List<String>> validate(T testView) {
        Map<String, List<String>> results = new HashMap<>();
        validateTest(testView, results);

        if (testView instanceof TestCreateView) {
            validateSelectCourse((TestCreateView) testView, results);
        }

        if (testView.getQuestions() == null || testView.getQuestions().isEmpty()) {
            results.put("questions", List.of("Нужно добавить хотя бы 1 вопрос"));
        } else {
            validateQuestions(testView.getQuestions(), results);
        }
        return results;
    }

    private <A extends AbstractAnswerView> void validateQuestions(List<? extends AbstractQuestionView<A>> questions,
        Map<String, List<String>> results) {
        for (int i = 0; i < questions.size(); i++) {
            List<String> error = new ArrayList<>();
            String key = "questions[%d]".formatted(i);
            AbstractQuestionView<A> question = questions.get(i);

            if (question.getQuestionType() == null) {
                error.add("Необходимо выбрать тип вопроса");
            }

            String text = question.getText();
            if (text == null || text.isBlank()) {
                error.add("Введите текст вопроса");
            }

            List<A> answers = question.getAnswers();
            if (answers == null || answers.size() < 2) {
                error.add("Должно быть добавлено не меньше 2 ответов");
            } else {
                if (existsMoreThanOneCorrectAnswerForOneAnswerQuestion(answers, question.getQuestionType())) {
                    error.add("Для вопроса с одним ответом не может быть добавлено несколько правильных ответов");
                }
                if (!existsOneCorrectAnswer(answers)) {
                    error.add("Должен быть хотя бы 1 правильный ответ на вопрос");
                }
                validateAnswers(answers, key, results);
            }

            if (!error.isEmpty()) {
                results.put(key, error);
            }
        }
    }

    private <A extends AbstractAnswerView>
    void validateAnswers(List<A> answers, String questionKey, Map<String, List<String>> results) {
        for (int i = 0; i < answers.size(); i++) {
            A answer = answers.get(i);
            String answerKey = questionKey + ".answers[%d]".formatted(i);
            List<String> error = new ArrayList<>();

            String text = answer.getText();
            if (text == null || text.isBlank()) {
                error.add("Нужно заполнить текст ответа");
            }

            if (!error.isEmpty()) {
                results.put(answerKey, error);
            }
        }
    }

    private <A extends AbstractAnswerView> boolean existsMoreThanOneCorrectAnswerForOneAnswerQuestion(List<A> answers, QuestionType questionType) {
        if (answers == null || answers.isEmpty()) {
            return false;
        }
        if (questionType != null && questionType.equals(QuestionType.ONE_ANSWER)) {
            return answers.stream()
                .filter(a -> a.getIsCorrect() != null && a.getIsCorrect())
                .count() > 1;
        }
        return false;
    }

    private <A extends AbstractAnswerView> boolean existsOneCorrectAnswer(List<A> answers) {
        if (answers == null || answers.isEmpty()) {
            return false;
        }
        return answers.stream()
            .filter(a -> a.getIsCorrect() != null)
            .anyMatch(AbstractAnswerView::getIsCorrect);
    }

    private <T extends AbstractTestView<?>> void validateTest(T testView, Map<String, List<String>> results) {
        String title = testView.getTitle();
        if (title == null || title.isBlank()) {
            results.put("title", List.of("Название теста не может быть пустым"));
        }

        Integer needCorrectAnswerCount = testView.getNeedCorrectAnswerCount();
        if (needCorrectAnswerCount == null || needCorrectAnswerCount <= 0) {
            results.put("needCorrectAnswerCount",
                List.of("Необходимо указать количество правильных ответов для прохождения теста"));
        } else if (testView.getQuestions() != null && !testView.getQuestions().isEmpty()) {
            if (testView.getQuestions().size() < needCorrectAnswerCount) {
                results.put("needCorrectAnswerCount",
                    List.of("Количество правильных ответов для прохождения теста не может быть больше количества вопросов"));
            }
        }
    }

    private void validateSelectCourse(TestCreateView testCreateView, Map<String, List<String>> results) {
        if (testCreateView.getCourseId() == null) {
            results.put("courseId", List.of("Нужно выбрать курс, к которому относится данный тест"));
        }
    }
}