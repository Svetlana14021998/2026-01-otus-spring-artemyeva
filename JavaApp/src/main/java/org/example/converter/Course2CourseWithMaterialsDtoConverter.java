package org.example.converter;

import lombok.RequiredArgsConstructor;
import org.example.model.Course;
import org.example.dto.course.CourseWithMaterialsDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Course2CourseWithMaterialsDtoConverter {

    private final AppUserToTeacherDtoConverter teacherDtoConverter;

    private final TheoryMaterial2TheoryMaterialDtoConverter theoryMaterialDtoConverter;

    public CourseWithMaterialsDto convert(Course course) {
        return CourseWithMaterialsDto.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .teacher(teacherDtoConverter.convert(course.getAuthor()))
            .materialsCount(course.getMaterials().size())
            .testId(course.getTest() != null ? course.getTest().getId() : null)
            .theoryMaterials(course.getMaterials().stream()
                .map(theoryMaterialDtoConverter::convert)
                .toList())
            .build();
    }
}
