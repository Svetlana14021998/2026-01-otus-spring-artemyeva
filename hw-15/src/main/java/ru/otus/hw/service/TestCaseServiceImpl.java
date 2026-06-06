package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.Specification;
import ru.otus.hw.model.TestCase;
import ru.otus.hw.model.TestStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Override
    public List<TestCase> createTestCases(Specification specification) {
        List<TestCase> testCases = new ArrayList<>();
        int count = (int) (Math.random() * 4) + 1;
        for (int i = 0; i < count; i++) {
            long number = i + 1;
            var testCase = create(specification, number);
            testCases.add(testCase);
        }
        return testCases;
    }

    private TestCase create(Specification specification, long numb) {
        long id = specification.getId() * 10 + numb;
        return TestCase.builder()
            .id(id)
            .name("Test case #" + id)
            .description("Description for test case #" + id)
            .priority((int) (Math.random() * 5) + 1)
            .status(TestStatus.values()[(int) (Math.random() * 2) + 1])
            .needAutotest(((int) (Math.random() * 2) + 1) == 1)
            .build();
    }
}
