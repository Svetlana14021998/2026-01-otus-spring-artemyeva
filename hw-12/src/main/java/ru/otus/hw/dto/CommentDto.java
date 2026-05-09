package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDto {

    private long id;

    private String text;

    private long bookId;

    private String bookTitle;
}
