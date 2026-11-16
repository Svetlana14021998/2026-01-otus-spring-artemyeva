package org.example.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;

import java.util.List;

@Data
@Builder
public class CourseUpdateDto {

    private Long id;

    @NotNull(message = "{validation.course.type.required}")
    private CourseType courseType;

    @NotBlank(message = "{validation.course.description.required}")
    private String description;

    @NotNull(message = "{validation.course.theory.materials.required}")
    private List<String> theoryMaterials;
}
