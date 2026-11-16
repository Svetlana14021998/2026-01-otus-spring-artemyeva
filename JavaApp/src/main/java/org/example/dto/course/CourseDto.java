package org.example.dto.course;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;
import org.example.dto.TeacherDto;

@Data
@Builder
public class CourseDto {

    private Long id;

    private CourseType courseType;

    private String description;

    private TeacherDto teacher;

    private int materialsCount;

    private Long testId;
}
