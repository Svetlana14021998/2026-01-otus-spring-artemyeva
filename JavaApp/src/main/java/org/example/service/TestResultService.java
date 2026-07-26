package org.example.service;

import org.example.enums.CourseType;
import org.example.model.TestResult;
import org.example.view.test.TestResultView;
import org.springframework.data.domain.Page;

public interface TestResultService {

    void deleteByUserId(Long userId);

    Page<TestResultView> findByUserId(Long userId, Boolean isOnlyLatestResults, Boolean isPassed,
        Long testId, CourseType courseType, int pageNumber, int pageSize);

    TestResultView save(TestResult testResult);
}
