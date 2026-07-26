package org.example.view.test.create;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.view.test.base.AbstractQuestionView;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class QuestionCreateView extends AbstractQuestionView<AnswerCreateView> {

}
