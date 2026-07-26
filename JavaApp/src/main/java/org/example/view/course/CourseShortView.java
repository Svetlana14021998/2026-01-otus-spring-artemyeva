package org.example.view.course;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;

@Data
@Builder
public class CourseShortView {

    private Long id;

    private CourseType courseType;

    private String description;
}
