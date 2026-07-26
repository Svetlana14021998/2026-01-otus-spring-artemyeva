package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.Test2TestShortViewConverter;
import org.example.converter.Test2TestViewConverter;
import org.example.converter.Test2TestWithOnlyTitleViewConverter;
import org.example.converter.TestCreateView2TestConverter;
import org.example.converter.TestView2TestConverter;
import org.example.enums.CourseType;
import org.example.exception.EntityLinksException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.PermissionException;
import org.example.model.Course;
import org.example.model.Test;
import org.example.model.TestResult;
import org.example.repository.TestRepository;
import org.example.repository.specification.TestSpecification;
import org.example.service.CourseService;
import org.example.service.TestResultService;
import org.example.service.TestService;
import org.example.view.test.TestResultView;
import org.example.view.test.TestSubmitView;
import org.example.view.test.create.TestCreateView;
import org.example.view.test.main.TestView;
import org.example.view.test.show.TestShortView;
import org.example.view.test.show.TestWithOnlyTitleView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    private final Test2TestShortViewConverter test2TestShortViewConverter;

    private final Test2TestViewConverter test2TestViewConverter;

    private final TestView2TestConverter testView2TestConverter;

    private final TestResultService testResultService;

    private final Test2TestWithOnlyTitleViewConverter test2TestWithOnlyTitleViewConverter;

    private final TestCreateView2TestConverter testCreateView2TestConverter;

    private final CourseService courseService;

    @Transactional(readOnly = true)
    @Override
    public Page<TestShortView> findAll(CourseType courseType, String title, Long authorId, int pageSize, int pageNumber) {
        log.info("Поиск всех тестов");
        Specification<Test> spec = buildSpecification(courseType, title, authorId);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return testRepository.findAll(spec, pageable).map(test2TestShortViewConverter::convert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestWithOnlyTitleView> findAll() {
        log.info("Поиск всех тестов (краткая информация)");
        return testRepository.findAll().stream()
            .map(test2TestWithOnlyTitleViewConverter::convert)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TestView findById(Long id) {
        log.info("Поиск теста id {}", id);
        return test2TestViewConverter.convert(testRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Тест %d не найден".formatted(id))));
    }

    @Transactional
    @Override
    public TestResultView processTestResult(TestSubmitView testSubmitView, Long userId) {
        log.info("Получены результаты теста id {} для пользователя {}", testSubmitView.getTestId(), userId);
        Long testId = testSubmitView.getTestId();
        Test test = testRepository.findById(testSubmitView.getTestId())
            .orElseThrow(() -> new EntityNotFoundException("Тест %d не найден".formatted(testId)));
        int rightAnswerCount = getRightAnswerCount(testSubmitView, test);
        int questionSize = test.getQuestions().size();
        BigDecimal percent = BigDecimal.valueOf((double) rightAnswerCount * 100 / questionSize)
            .setScale(2, RoundingMode.HALF_UP);
        boolean isPassed = rightAnswerCount >= test.getNeedCorrectAnswerCount();
        log.info("Правильных ответов {} из {}, % правильных ответов {}, тест пройден: {}",
            rightAnswerCount, questionSize, percent, isPassed ? "Да" : "Нет");

        TestResult testResult = TestResult.builder()
            .test(test)
            .userId(userId)
            .correctAnswers(rightAnswerCount)
            .totalQuestions(questionSize)
            .percentage(percent)
            .isPassed(isPassed)
            .build();

        return testResultService.save(testResult);
    }

    @Transactional(readOnly = true)
    @Override
    public long testsCount() {
        log.info("Получение количества тестов");
        long count = testRepository.count();
        log.info("Найдено {} тестов", count);
        return count;
    }

    @Transactional
    @Override
    public void deleteById(Long id, Long authorId) {
        log.info("Удаление теста id {}", id);
        Test test = testRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Тест id=%d не найден".formatted(id)));
        Course course = test.getCourse();
        if (!Objects.equals(course.getAuthor().getId(), authorId)) {
            throw new PermissionException("Тест может быть удален только преподавателем, который его создал",
                "Вы не можете удалить Тест, созданный другим преподавателем");
        }

        course.setTest(null);
        testRepository.delete(test);

        log.info("Тест id {} удален", id);
    }

    @Transactional
    @Override
    public void createTest(TestCreateView testCreateView, Long authorId) {
        log.info("Создание теста");
        Course course = courseService.findEntityById(testCreateView.getCourseId());
        if (course.getTest() != null) {
            throw new EntityLinksException("У курса уже есть тест", "К данному курсу уже добавлен тест");
        }
        if (!Objects.equals(course.getAuthor().getId(), authorId)) {
            throw new PermissionException("Тест может быть создан только преподавателем, который создал курс",
                "Вы не можете создать тест к курсу, который создал другой преподаватель");
        }
        Test test = testCreateView2TestConverter.convert(testCreateView);
        course.setTest(test);
        test.setCourse(course);

        courseService.update(course);
        log.info("Тест id {} создан", test.getId());
    }

    @Transactional
    @Override
    public void updateTest(TestView testView, Long authorId) {
        log.info("Изменение теста {}", testView.getId());
        Test test = testRepository.findById(testView.getId())
            .orElseThrow(() -> new EntityNotFoundException("Тест %s не найден".formatted(testView.getId())));
        Course course = test.getCourse();
        if (!Objects.equals(course.getAuthor().getId(), authorId)) {
            throw new PermissionException("Тест может быть изменен только преподавателем, который его создал",
                "Вы не можете изменить Тест, созданный другим преподавателем");
        }
        test = testView2TestConverter.convert(test, testView);
        testRepository.save(test);
        log.info("Тест {} был изменен", testView.getId());
    }

    private int getRightAnswerCount(TestSubmitView testSubmitView, Test test) {
        Map<Long, List<Long>> correctAnswersMap = test.getCorrectAnswersMap();
        Map<Long, List<Long>> userAnswers = testSubmitView.getAnswers();

        int rightAnswerCount = 0;
        for (Long key : correctAnswersMap.keySet()) {
            if (userAnswers.containsKey(key)) {
                List<Long> correctAnswer = correctAnswersMap.get(key);
                List<Long> userAnswer = userAnswers.get(key);
                if (userAnswer.containsAll(correctAnswer)) {
                    rightAnswerCount++;
                }
            }
        }
        return rightAnswerCount;
    }

    private Specification<Test> buildSpecification(CourseType courseType, String title, Long authorId) {
        Specification<Test> spec = Specification.allOf();
        if (courseType != null) {
            spec = spec.and(TestSpecification.courseTypeIs(courseType));
        }
        if ((title != null && !title.isBlank())) {
            spec = spec.and(TestSpecification.titleIsEquals(title));
        }
        if (authorId != null) {
            spec = spec.and(TestSpecification.authorIdIs(authorId));
        }
        return spec;
    }
}
