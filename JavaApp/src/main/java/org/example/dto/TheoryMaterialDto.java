package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheoryMaterialDto {

    private Long id;

    private String content;

    private Long courseId;
}
