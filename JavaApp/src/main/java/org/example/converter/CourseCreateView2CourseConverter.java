package org.example.converter;

import org.example.model.AppUser;
import org.example.model.Course;
import org.example.model.TheoryMaterial;
import org.example.view.course.CourseCreateView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseCreateView2CourseConverter {

    public Course convert(CourseCreateView courseCreateView, AppUser user) {
        Course course = Course.builder()
            .courseType(courseCreateView.getCourseType())
            .description(courseCreateView.getDescription())
            .author(user)
            .build();

        List<TheoryMaterial> materials = courseCreateView.getTheoryMaterials().stream()
            .map(x -> TheoryMaterial.builder()
                .content(x)
                .course(course)
                .build())
            .toList();
        course.setMaterials(materials);
        return course;
    }
}
