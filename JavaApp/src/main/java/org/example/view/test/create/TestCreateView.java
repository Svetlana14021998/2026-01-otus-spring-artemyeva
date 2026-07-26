package org.example.view.test.create;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.view.test.base.AbstractTestView;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestCreateView extends AbstractTestView<QuestionCreateView> {

    private String title;

    private Integer needCorrectAnswerCount;

    private Long courseId;

    private List<QuestionCreateView> questions;
}
