package org.example.view.test.main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.view.test.base.AbstractTestView;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestView extends AbstractTestView<QuestionView> {

    private Long id;
}
