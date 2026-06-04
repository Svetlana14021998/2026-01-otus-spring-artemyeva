package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AutoTest {

    private String id;

    private String displayName;

    private String code;

    private Long testCaseId;

    private String label;
}
