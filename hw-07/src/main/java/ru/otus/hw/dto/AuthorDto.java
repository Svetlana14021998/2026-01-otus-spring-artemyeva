package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthorDto {

    private long id;

    private String fullName;
}
