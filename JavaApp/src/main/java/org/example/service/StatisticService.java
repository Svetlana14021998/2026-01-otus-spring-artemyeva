package org.example.service;

import org.example.enums.CourseType;
import org.example.view.TeacherView;
import org.example.view.course.CourseView;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    Double getAveragePassPercentage();

    Map<CourseType, Double> getAveragePercentageByCourseType();

    List<CourseView> findByTestIdIsNull();

    Map<String, Double> getAveragePercentageByTest();

    List<CourseType> courseTypeWithoutCourses();

    Map<TeacherView, Long> getTeachersCourseCount();

    Map<TeacherView, Double> getAveragePercentageByTeacher();
}
