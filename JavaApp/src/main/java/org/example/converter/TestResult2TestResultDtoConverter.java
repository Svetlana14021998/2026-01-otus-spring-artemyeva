package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.dto.test.TestResultDto;
import org.example.model.TestResult;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestResult2TestResultDtoConverter {

    private final MessageSource messageSourceConfig;

    public TestResultDto convert(TestResult testResult) {
        String PASSED = messageSourceConfig.getMessage("test.passed", null, LocaleContextHolder.getLocale());
        String NOT_PASSED =
            messageSourceConfig.getMessage("test.not.passed", null, LocaleContextHolder.getLocale());

        return TestResultDto.builder()
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
