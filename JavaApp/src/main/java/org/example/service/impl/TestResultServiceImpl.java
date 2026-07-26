package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.TestResult2TestResultViewConverter;
import org.example.enums.CourseType;
import org.example.model.TestResult;
import org.example.repository.TestResultRepository;
import org.example.repository.specification.TestResultSpecification;
import org.example.service.TestResultService;
import org.example.view.test.TestResultView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestResultServiceImpl implements TestResultService {

    private final TestResultRepository testResultRepository;

    private final TestResult2TestResultViewConverter testResult2TestResultViewConverter;

    @Transactional
    @Override
    public void deleteByUserId(Long userId) {
        log.info("Удаление результатов тестирования для  пользователя id {}", userId);
        testResultRepository.deleteByUserId(userId);
        log.info("Результаты для пользователя id {} удалены", userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TestResultView> findByUserId(Long userId, Boolean isOnlyLatestResults, Boolean isPassed, Long testId, CourseType courseType, int pageNumber,
        int pageSize) {
        log.info("Поиск результатов тестирования для пользователя id {}", userId);
        Specification<TestResult> spec = buildSpecification(userId, isOnlyLatestResults, isPassed, testId, courseType);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return testResultRepository.findAll(spec, pageable)
            .map(testResult2TestResultViewConverter::convert);
    }

    @Transactional
    @Override
    public TestResultView save(TestResult testResult) {
        log.info("Сохранение результатов теста {} для пользователя id {}", testResult.getTest().getTitle(), testResult.getUserId());
        return testResult2TestResultViewConverter.convert(testResultRepository.save(testResult));
    }

    private Specification<TestResult> buildSpecification(Long userId, Boolean isOnlyLatestResults, Boolean isPassed, Long testId, CourseType courseType) {
        Specification<TestResult> spec = Specification.allOf(TestResultSpecification.userIdIs(userId));

        if (isOnlyLatestResults != null) {
            spec = spec.and(TestResultSpecification.latestResults(isPassed));
        }
        if (isPassed != null) {
            spec = spec.and(TestResultSpecification.passedIs(isPassed));
        }
        if (testId != null) {
            spec = spec.and(TestResultSpecification.testIdIs(testId));
        }
        if (courseType != null) {
            spec = spec.and(TestResultSpecification.courseTypeIs(courseType));
        }
        return spec;
    }
}
