package org.example.view.test;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Получаемые результаты тестирования от пользователя
 */
@Data
@Builder
public class TestSubmitView {

    private Long testId;

    private Map<Long, List<Long>> answers;
}
