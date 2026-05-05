package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDto {

    private Long id;

    private String text;

    private Long bookId;
}
