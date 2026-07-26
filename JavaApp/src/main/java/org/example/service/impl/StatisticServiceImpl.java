package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.Course2CourseViewConverter;
import org.example.enums.CourseType;
import org.example.repository.CourseRepository;
import org.example.repository.TestRepository;
import org.example.repository.TestResultRepository;
import org.example.repository.UserRepository;
import org.example.service.StatisticService;
import org.example.view.TeacherView;
import org.example.view.course.CourseView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final TestResultRepository testResultRepository;

    private final CourseRepository courseRepository;

    private final Course2CourseViewConverter course2CourseViewConverter;

    private final TestRepository testRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Double getAveragePassPercentage() {
        log.info("Получение среднего результата прохождения тестов");
        Double percentage = testResultRepository.getAveragePassPercentage();
        log.info("Средний процент прохождения тестов {}", percentage);
        return percentage;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<CourseType, Double> getAveragePercentageByCourseType() {
        log.info("Получение среднего результата прохождения тестов по разделу курсов");
        Map<CourseType, Double> result = new LinkedHashMap<>();

        courseRepository.getAveragePercentageByCourseType()
            .forEach(row -> {
                CourseType key = (CourseType) row[0];
                result.put(key, (Double) row[1]);
            });
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseView> findByTestIdIsNull() {
        log.info("Поиск курсов, по которым нет тестов");
        List<CourseView> courses = courseRepository.findByTestIdIsNull()
            .stream().map(course2CourseViewConverter::convert)
            .toList();
        log.info("Найдено {} курсов без тестов", courses.size());
        return courses;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Double> getAveragePercentageByTest() {
        log.info("Получение среднего результата прохождения каждого теста");
        Map<String, Double> result = new LinkedHashMap<>();

        testRepository.getAveragePercentageByTest()
            .forEach(row -> {
                String key = (String) row[0];
                result.put(key, (Double) row[1]);
            });

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseType> courseTypeWithoutCourses() {
        log.info("Поиск разделов, по которым нет курсов");
        List<CourseType> courseTypes = new ArrayList<>(List.of(CourseType.values()));
        List<CourseType> courseTypeInDb = courseRepository.findDistinctByCourseType();
        courseTypes.removeAll(courseTypeInDb);
        log.info("Найдено {} разделов, по которым нет курсов", courseTypes.size());
        return courseTypes;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<TeacherView, Long> getTeachersCourseCount() {
        log.info("Получение количества курсов для каждого учителя");
        Map<TeacherView, Long> result = new LinkedHashMap<>();
        userRepository.getTeachersCourseCount()
            .forEach(row -> {
                TeacherView view = TeacherView.builder()
                    .id((Long) row[0])
                    .fullName((String) row[1])
                    .build();
                result.put(view, (Long) row[2]);
            });
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<TeacherView, Double> getAveragePercentageByTeacher() {
        log.info("Получение среднего результата прохождения тестов каждого учителя");
        Map<TeacherView, Double> result = new LinkedHashMap<>();
        courseRepository.getAveragePercentageByTeacher()
            .forEach(row -> {
                TeacherView view = TeacherView.builder()
                    .id((Long) row[0])
                    .fullName((String) row[1])
                    .build();
                result.put(view, (Double) row[2]);
            });
        return result;
    }
}
