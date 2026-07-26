package org.example.view.test.main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.view.test.base.AbstractAnswerView;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AnswerView extends AbstractAnswerView {

    private Long id;
}
