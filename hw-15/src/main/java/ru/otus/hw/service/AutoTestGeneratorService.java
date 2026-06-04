package ru.otus.hw.service;

import ru.otus.hw.model.TestCase;

public interface AutoTestGeneratorService {

    TestCase generateAutotests(TestCase testCase);

    TestCase generateAutotestsWithHighPriority(TestCase testCase);
}
