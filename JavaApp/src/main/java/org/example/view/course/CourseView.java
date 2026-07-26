package org.example.view.course;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;
import org.example.view.TeacherView;

@Data
@Builder
public class CourseView {

    private Long id;

    private CourseType courseType;

    private String description;

    private TeacherView teacher;

    private int materialsCount;

    private Long testId;
}
