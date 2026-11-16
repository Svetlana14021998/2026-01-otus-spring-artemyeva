package org.example.dto.course;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;
import org.example.dto.TeacherDto;
import org.example.dto.TheoryMaterialDto;

import java.util.List;

@Data
@Builder
public class CourseWithMaterialsDto {

    private Long id;

    private CourseType courseType;

    private String description;

    private TeacherDto teacher;

    private int materialsCount;

    private Long testId;

    private List<TheoryMaterialDto> theoryMaterials;
}
