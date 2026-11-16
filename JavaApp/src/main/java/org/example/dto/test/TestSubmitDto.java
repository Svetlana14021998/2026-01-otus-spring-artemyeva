package org.example.dto.test;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Получаемые результаты тестирования от пользователя
 */
@Data
@Builder
public class TestSubmitDto {

    private Long testId;

    private Map<Long, List<Long>> answers;
}
