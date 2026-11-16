package org.example.dto.test.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public abstract class AbstractTestDto<Q extends AbstractQuestionDto<?>> {

    private String title;

    private Integer needCorrectAnswerCount;

    private Long courseId;

    private List<Q> questions;
}
