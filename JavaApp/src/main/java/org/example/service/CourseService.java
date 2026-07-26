package org.example.service;

import org.example.enums.CourseType;
import org.example.model.AppUser;
import org.example.model.Course;
import org.example.view.course.CourseCreateView;
import org.example.view.course.CourseShortView;
import org.example.view.course.CourseUpdateView;
import org.example.view.course.CourseView;
import org.example.view.course.CourseWithMaterialsView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    Page<CourseView> findAll(Long authorId, String description, CourseType courseType, int pageNumber, int pageSize);

    CourseView findById(Long id);

    String getCourseDescription(Long id);

    Course findEntityById(Long id);

    CourseShortView findByIdForTest(Long id);

    List<CourseShortView> findCourseWithoutTestForTeacher(Long userId);

    CourseUpdateView findByIdToEdit(Long id);

    CourseWithMaterialsView findByIdWithMaterials(Long id);

    long courseCount();

    void deleteById(Long id, Long userId);

    void save(CourseCreateView courseCreateView, AppUser user);

    void update(CourseUpdateView courseUpdateView, Long userId);

    void update(Course course);
}
