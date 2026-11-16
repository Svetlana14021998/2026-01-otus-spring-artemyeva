package org.example.dto.test.main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.dto.test.base.AbstractTestDto;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestDto extends AbstractTestDto<QuestionDto> {

    private Long id;
}
