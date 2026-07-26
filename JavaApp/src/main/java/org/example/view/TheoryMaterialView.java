package org.example.view;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheoryMaterialView {

    private Long id;

    private String content;

    private Long courseId;
}
