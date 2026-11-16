package org.example.converter;

import org.example.model.Course;
import org.example.model.TheoryMaterial;
import org.example.dto.course.CourseUpdateDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseUpdateDtoConverter {

    public CourseUpdateDto toDto(Course course) {
        List<String> materials = course.getMaterials().stream()
            .map(TheoryMaterial::getContent)
            .toList();

        return CourseUpdateDto.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .theoryMaterials(materials)
            .build();
    }

    public Course toEntity(Course course, CourseUpdateDto courseUpdateDto) {
        List<TheoryMaterial> materials = courseUpdateDto.getTheoryMaterials().stream()
            .map(x -> TheoryMaterial.builder()
                .course(course)
                .content(x)
                .build())
            .toList();
        course.setDescription(courseUpdateDto.getDescription());
        course.setCourseType(courseUpdateDto.getCourseType());
        course.getMaterials().clear();
        course.getMaterials().addAll(materials);
        return course;
    }
}
