package ru.otus.hw.models;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("books")
@Getter
public class Book {
    @Id
    private final Long id;

    @NotNull
    private final String title;

    @NotNull
    private final Long authorId;

    @PersistenceCreator
    public Book(Long id, @NotNull Long authorId, @NotNull String title) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
    }
}
