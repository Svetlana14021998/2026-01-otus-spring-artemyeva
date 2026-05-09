package ru.otus.hw.models;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("comments")
@Getter
public class Comment {

    @Id
    private final Long id;

    @NotNull
    private final String text;

    @NotNull
    private final Long bookId;

    @PersistenceCreator
    public Comment(Long id, @NotNull Long bookId, @NotNull String text) {
        this.id = id;
        this.bookId = bookId;
        this.text = text;
    }
}
