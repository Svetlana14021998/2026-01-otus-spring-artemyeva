package org.example.dto.test.create;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.dto.test.base.AbstractQuestionDto;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class QuestionCreateDto extends AbstractQuestionDto<AnswerCreateDto> {

}
