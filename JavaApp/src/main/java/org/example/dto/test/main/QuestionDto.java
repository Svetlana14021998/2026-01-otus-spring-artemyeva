package org.example.dto.test.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.dto.test.base.AbstractQuestionDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QuestionDto extends AbstractQuestionDto<AnswerDto> {

    private Long id;
}
