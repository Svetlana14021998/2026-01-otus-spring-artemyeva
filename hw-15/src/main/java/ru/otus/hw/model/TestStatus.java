package ru.otus.hw.model;

public enum TestStatus {
    ACTIVE("Актуальный"),
    DEPRECATED("Неактуальный"),
    DRAFT("Черновик"),
    NEED_CORRECTION("На доработке")
    ;

    private final String description;

    TestStatus(String description) {
        this.description = description;
    }
}
