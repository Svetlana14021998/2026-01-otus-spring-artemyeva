package org.example.view.test.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.enums.QuestionType;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractQuestionView<A extends AbstractAnswerView> {

    private QuestionType questionType;

    private String text;

    private List<A> answers;
}
