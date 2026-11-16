package org.example.dto.course;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;

@Data
@Builder
public class CourseShortDto {

    private Long id;

    private CourseType courseType;

    private String description;
}
