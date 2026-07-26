package org.example.converter;

import org.example.model.Course;
import org.example.model.TheoryMaterial;
import org.example.view.course.CourseUpdateView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseUpdateViewConverter {

    public CourseUpdateView toView(Course course) {
        List<String> materials = course.getMaterials().stream()
            .map(TheoryMaterial::getContent)
            .toList();

        return CourseUpdateView.builder()
            .id(course.getId())
            .courseType(course.getCourseType())
            .description(course.getDescription())
            .theoryMaterials(materials)
            .build();
    }

    public Course toEntity(Course course, CourseUpdateView courseUpdateView) {
        List<TheoryMaterial> materials = courseUpdateView.getTheoryMaterials().stream()
            .map(x -> TheoryMaterial.builder()
                .course(course)
                .content(x)
                .build())
            .toList();
        course.setDescription(courseUpdateView.getDescription());
        course.setCourseType(courseUpdateView.getCourseType());
        course.getMaterials().clear();
        course.getMaterials().addAll(materials);
        return course;
    }
}
