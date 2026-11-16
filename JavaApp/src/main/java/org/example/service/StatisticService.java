package org.example.service;

import org.example.enums.CourseType;
import org.example.dto.TeacherDto;
import org.example.dto.course.CourseDto;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    Double getAveragePassPercentage();

    Map<CourseType, Double> getAveragePercentageByCourseType();

    List<CourseDto> findByTestIdIsNull();

    Map<String, Double> getAveragePercentageByTest();

    List<CourseType> courseTypeWithoutCourses();

    Map<TeacherDto, Long> getTeachersCourseCount();

    Map<TeacherDto, Double> getAveragePercentageByTeacher();
}
