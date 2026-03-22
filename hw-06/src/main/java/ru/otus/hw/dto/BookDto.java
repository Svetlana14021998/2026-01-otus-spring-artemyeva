package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BookDto {

    private long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}
