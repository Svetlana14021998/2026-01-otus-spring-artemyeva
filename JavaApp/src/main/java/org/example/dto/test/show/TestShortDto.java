package org.example.dto.test.show;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;

/**
 * Используется для отображения информации о тесте без вопросов
 */
@Data
@Builder
public class TestShortDto {

    private Long id;

    private String title;

    private int needCorrectAnswerCount;

    private int questionsCount;

    private CourseType courseType;

    private Long courseId;

    private String author;
}
