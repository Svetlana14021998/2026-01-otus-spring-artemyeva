package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.AutoTest;
import ru.otus.hw.model.TestCase;

@Service
public class AutoTestGeneratorServiceImpl implements AutoTestGeneratorService {

    @Override
    public TestCase generateAutotests(TestCase testCase) {
        int count = (int) (Math.random() * 3) + 1;
        for (int i = 0; i < count; i++) {
            int number = i + 1;
            AutoTest autoTest = create(testCase, number);
            testCase.addAutotest(autoTest);
        }

        return testCase;
    }

    @Override
    public TestCase generateAutotestsWithHighPriority(TestCase testCase) {
        testCase = generateAutotests(testCase);
        return setHighPriority(testCase);
    }

    public TestCase setHighPriority(TestCase testCase) {
        testCase.getAutoTests().forEach(x -> x.setLabel("high"));
        return testCase;
    }

    private AutoTest create(TestCase testCase, int number) {
        Long testCaseId = testCase.getId();
        String id = "AUTO" + testCaseId + number;
        return AutoTest.builder()
            .id(id)
            .testCaseId(testCaseId)
            .code("some code for autotest " + id)
            .displayName("displayName for " + id)
            .label("normal")
            .build();
    }
}
