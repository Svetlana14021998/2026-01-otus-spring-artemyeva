package org.example.dto.test.base;

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
public abstract class AbstractQuestionDto<A extends AbstractAnswerDto> {

    private QuestionType questionType;

    private String text;

    private List<A> answers;
}
