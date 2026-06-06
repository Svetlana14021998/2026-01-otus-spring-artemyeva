package ru.otus.hw.model;

import lombok.Getter;

@Getter
public enum Type {
    PROCESS("Описание процесса"),
    OPERATION("Описание операции"),
    VALIDATION("Описание проверок");

    private final String name;

    Type(String name) {
        this.name = name;
    }
}
