package org.example.dto.test.main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.dto.test.base.AbstractAnswerDto;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AnswerDto extends AbstractAnswerDto {

    private Long id;
}
