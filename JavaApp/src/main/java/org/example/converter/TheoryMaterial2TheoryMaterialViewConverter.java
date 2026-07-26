package org.example.converter;

import org.example.model.TheoryMaterial;
import org.example.view.TheoryMaterialView;
import org.springframework.stereotype.Component;

@Component
public class TheoryMaterial2TheoryMaterialViewConverter {

    public TheoryMaterialView convert(TheoryMaterial material) {
        return TheoryMaterialView.builder()
            .id(material.getId())
            .courseId(material.getCourse().getId())
            .content(material.getContent())
            .build();
    }
}
