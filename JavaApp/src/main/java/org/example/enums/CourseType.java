package org.example.enums;

import lombok.Getter;

@Getter
public enum CourseType {

    OOP("OOP", "Принципы ООП"),

    SQL("SQL", "Язык запросов SQL"),

    MULTITHREADING("Multithreading", "Многопоточность"),

    EXCEPTION("Exceptions", "Исключения"),

    COLLECTIONS("Collections", "Коллекции");

    private final String code;
    private final String description;

    CourseType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
