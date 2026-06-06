package ru.otus.hw.service;

import ru.otus.hw.model.Specification;
import ru.otus.hw.model.TestCase;

import java.util.List;

public interface TestCaseService {

    List<TestCase> createTestCases(Specification specification);
}
