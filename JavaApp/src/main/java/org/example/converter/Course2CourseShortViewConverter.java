package org.example.converter;

import org.example.model.Course;
import org.example.view.course.CourseShortView;
import org.springframework.stereotype.Component;

@Component
public class Course2CourseShortViewConverter {

    public CourseShortView convert(Course course) {
        return CourseShortView.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .build();
    }
}
