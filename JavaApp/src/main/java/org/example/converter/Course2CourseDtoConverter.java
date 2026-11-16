package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Course;
import org.example.dto.course.CourseDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Course2CourseDtoConverter {

    private final AppUserToTeacherDtoConverter teacherDtoConverter;

    public CourseDto convert(Course course) {
        return CourseDto.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .teacher(teacherDtoConverter.convert(course.getAuthor()))
            .materialsCount(course.getMaterials().size())
            .testId(course.getTest() != null ? course.getTest().getId() : null)
            .build();
    }
}
