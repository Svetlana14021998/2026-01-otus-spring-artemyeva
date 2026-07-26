package org.example.enums;

import lombok.Getter;

@Getter
public enum QuestionType {

    ONE_ANSWER("One answer", "Один ответ"),
    MULTI_ANSWERS("Some answers", "Несколько ответов");

    private final String code;
    private final String description;

    QuestionType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
