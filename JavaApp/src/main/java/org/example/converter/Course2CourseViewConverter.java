package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Course;
import org.example.view.course.CourseView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Course2CourseViewConverter {

    private final AppUserToTeacherViewConverter teacherViewConverter;

    public CourseView convert(Course course) {
        return CourseView.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .teacher(teacherViewConverter.convert(course.getAuthor()))
            .materialsCount(course.getMaterials().size())
            .testId(course.getTest() != null ? course.getTest().getId() : null)
            .build();
    }
}
