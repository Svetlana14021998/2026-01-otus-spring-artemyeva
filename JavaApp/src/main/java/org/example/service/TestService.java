package org.example.service;

import org.example.enums.CourseType;
import org.example.dto.test.TestResultDto;
import org.example.dto.test.TestSubmitDto;
import org.example.dto.test.create.TestCreateDto;
import org.example.dto.test.main.TestDto;
import org.example.dto.test.show.TestShortDto;
import org.example.dto.test.show.TestWithOnlyTitleDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TestService {

    Page<TestShortDto> findAll(CourseType courseType, String title, Long authorId, int pageSize, int pageNumber);

    List<TestWithOnlyTitleDto> findAll();

    TestDto findById(Long id);

    TestResultDto processTestResult(TestSubmitDto testSubmitDto, Long userId);

    long testsCount();

    void deleteById(Long id, Long authorId);

    void createTest(TestCreateDto testCreateDto, Long id);

    void updateTest(TestDto testDto, Long authorId);
}

