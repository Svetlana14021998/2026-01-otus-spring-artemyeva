package ru.otus.hw.models;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("authors")
@Getter
public class Author {

    @Id
    private final Long id;

    @NotNull
    private final String fullName;

    @PersistenceCreator
    public Author(@NotNull String fullName, long id) {
        this.fullName = fullName;
        this.id = id;
    }
}
