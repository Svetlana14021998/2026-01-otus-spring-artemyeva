package org.example.converter;

import org.example.model.TestResult;
import org.example.view.test.TestResultView;
import org.springframework.stereotype.Component;

@Component
public class TestResult2TestResultViewConverter {

    public TestResultView convert(TestResult testResult) {
        String PASSED = "Вы успешно прошли тест";
        String NOT_PASSED = "Вы не набрали нужного количества правильных ответов";

        return TestResultView.builder()
            .testId(testResult.getTest().getId())
            .testTitle(testResult.getTest().getTitle())
            .correctAnswers(testResult.getCorrectAnswers())
            .totalQuestions(testResult.getTotalQuestions())
            .percentage(testResult.getPercentage())
            .passed(testResult.isPassed())
            .courseId(testResult.getTest().getCourse().getId())
            .message(testResult.isPassed() ? PASSED : NOT_PASSED)
            .passedDate(testResult.getPassedDate())
            .courseType(testResult.getTest().getCourse().getCourseType())
            .build();
    }
}
