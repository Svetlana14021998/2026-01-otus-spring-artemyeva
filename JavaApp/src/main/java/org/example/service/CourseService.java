package org.example.service;

import org.example.dto.course.CourseCreateDto;
import org.example.enums.CourseType;
import org.example.model.AppUser;
import org.example.model.Course;
import org.example.dto.course.CourseShortDto;
import org.example.dto.course.CourseUpdateDto;
import org.example.dto.course.CourseDto;
import org.example.dto.course.CourseWithMaterialsDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    Page<CourseDto> findAll(Long authorId, String description, CourseType courseType, int pageNumber, int pageSize);

    CourseDto findById(Long id);

    String getCourseDescription(Long id);

    Course findEntityById(Long id);

    CourseShortDto findByIdForTest(Long id);

    List<CourseShortDto> findCourseWithoutTestForTeacher(Long userId);

    CourseUpdateDto findByIdToEdit(Long id);

    CourseWithMaterialsDto findByIdWithMaterials(Long id);

    long courseCount();

    void deleteById(Long id, Long userId);

    void save(CourseCreateDto courseCreateDto, AppUser user);

    void update(CourseUpdateDto courseUpdateDto, Long userId);

    void update(Course course);
}
