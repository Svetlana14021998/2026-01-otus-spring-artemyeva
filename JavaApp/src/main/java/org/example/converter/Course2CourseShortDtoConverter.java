package org.example.converter;

import org.example.model.Course;
import org.example.dto.course.CourseShortDto;
import org.springframework.stereotype.Component;

@Component
public class Course2CourseShortDtoConverter {

    public CourseShortDto convert(Course course) {
        return CourseShortDto.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .build();
    }
}
