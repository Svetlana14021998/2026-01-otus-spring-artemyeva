package org.example.dto.test;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Результат прохождения теста из БД
 */
@Builder
@Data
public class TestResultDto {

    private String testTitle;

    private Long testId;

    private int correctAnswers;

    private int totalQuestions;

    private BigDecimal percentage;

    private boolean passed;

    private String message;

    private Long courseId;

    private LocalDateTime passedDate;

    private CourseType courseType;
}
