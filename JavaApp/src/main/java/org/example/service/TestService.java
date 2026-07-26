package org.example.service;

import org.example.enums.CourseType;
import org.example.view.test.TestResultView;
import org.example.view.test.TestSubmitView;
import org.example.view.test.create.TestCreateView;
import org.example.view.test.main.TestView;
import org.example.view.test.show.TestShortView;
import org.example.view.test.show.TestWithOnlyTitleView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TestService {

    Page<TestShortView> findAll(CourseType courseType, String title, Long authorId, int pageSize, int pageNumber);

    List<TestWithOnlyTitleView> findAll();

    TestView findById(Long id);

    TestResultView processTestResult(TestSubmitView testSubmitView, Long userId);

    long testsCount();

    void deleteById(Long id, Long authorId);

    void createTest(TestCreateView testCreateView, Long id);

    void updateTest(TestView testView, Long authorId);
}

