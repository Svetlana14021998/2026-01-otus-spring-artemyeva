package org.example.view.test.show;

import lombok.Builder;
import lombok.Data;

/**
 * Краткая информация о тесте (используется для фильтрации результатов тестирования)
 */
@Data
@Builder
public class TestWithOnlyTitleView {

    private Long id;

    private String title;
}
