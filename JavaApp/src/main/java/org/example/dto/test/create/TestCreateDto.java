package org.example.dto.test.create;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.dto.test.base.AbstractTestDto;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestCreateDto extends AbstractTestDto<QuestionCreateDto> {

    private String title;

    private Integer needCorrectAnswerCount;

    private Long courseId;

    private List<QuestionCreateDto> questions;
}
