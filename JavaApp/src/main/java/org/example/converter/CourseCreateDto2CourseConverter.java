package org.example.converter;

import org.example.dto.course.CourseCreateDto;
import org.example.model.AppUser;
import org.example.model.Course;
import org.example.model.TheoryMaterial;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseCreateDto2CourseConverter {

    public Course convert(CourseCreateDto courseCreateDto, AppUser user) {
        Course course = Course.builder()
            .courseType(courseCreateDto.getCourseType())
            .description(courseCreateDto.getDescription())
            .author(user)
            .build();

        List<TheoryMaterial> materials = courseCreateDto.getTheoryMaterials().stream()
            .map(x -> TheoryMaterial.builder()
                .content(x)
                .course(course)
                .build())
            .toList();
        course.setMaterials(materials);
        return course;
    }
}
