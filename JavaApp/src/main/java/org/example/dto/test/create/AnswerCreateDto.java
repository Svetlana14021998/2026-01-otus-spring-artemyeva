package org.example.dto.test.create;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.dto.test.base.AbstractAnswerDto;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnswerCreateDto extends AbstractAnswerDto {

}
