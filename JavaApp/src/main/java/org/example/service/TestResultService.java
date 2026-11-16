package org.example.service;

import org.example.enums.CourseType;
import org.example.model.TestResult;
import org.example.dto.test.TestResultDto;
import org.springframework.data.domain.Page;

public interface TestResultService {

    void deleteByUserId(Long userId);

    Page<TestResultDto> findByUserId(Long userId, Boolean isOnlyLatestResults, Boolean isPassed,
        Long testId, CourseType courseType, int pageNumber, int pageSize);

    TestResultDto save(TestResult testResult);
}
