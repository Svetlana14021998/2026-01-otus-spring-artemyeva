package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TestCase {

    private Long id;

    private String customerName;

    private Integer priority;

    private String name;

    private String description;

    private TestStatus status;

    private Long specificationId;

    private boolean needAutotest;

    @Builder.Default
    private List<AutoTest> autoTests = new ArrayList<>();

    public void addAutotest(AutoTest autoTest) {
        autoTests.add(autoTest);
    }
}
