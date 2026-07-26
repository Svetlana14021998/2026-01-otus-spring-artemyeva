package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Course;
import org.example.view.course.CourseWithMaterialsView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Course2CourseViewMaterialsConverter {

    private final AppUserToTeacherViewConverter teacherViewConverter;

    private final TheoryMaterial2TheoryMaterialViewConverter theoryMaterialViewConverter;

    public CourseWithMaterialsView convert(Course course) {
        return CourseWithMaterialsView.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .teacher(teacherViewConverter.convert(course.getAuthor()))
            .materialsCount(course.getMaterials().size())
            .testId(course.getTest() != null ? course.getTest().getId() : null)
            .theoryMaterials(course.getMaterials().stream()
                .map(theoryMaterialViewConverter::convert)
                .toList())
            .build();
    }
}
