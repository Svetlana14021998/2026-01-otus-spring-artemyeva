package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Specification {

    private Long id;

    private Type type;

    private String text;

    private String author;
}
