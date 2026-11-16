package org.example.converter;

import org.example.model.TheoryMaterial;
import org.example.dto.TheoryMaterialDto;
import org.springframework.stereotype.Component;

@Component
public class TheoryMaterial2TheoryMaterialDtoConverter {

    public TheoryMaterialDto convert(TheoryMaterial material) {
        return TheoryMaterialDto.builder()
            .id(material.getId())
            .courseId(material.getCourse().getId())
            .content(material.getContent())
            .build();
    }
}
