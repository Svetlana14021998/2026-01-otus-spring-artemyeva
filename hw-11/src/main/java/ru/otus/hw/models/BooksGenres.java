package ru.otus.hw.models;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("books_genres")
@Getter
public class BooksGenres {

    @Id
    private Long id;

    @NotNull
    private final Long bookId;

    @NotNull
    private final Long genreId;

    @PersistenceCreator
    public BooksGenres(Long id, @NotNull Long bookId, @NotNull Long genreId) {
        this.id = id;
        this.bookId = bookId;
        this.genreId = genreId;
    }

    public BooksGenres(@NotNull Long bookId, @NotNull Long genreId) {
        this.bookId = bookId;
        this.genreId = genreId;
    }
}
