package ru.otus.hw.models;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("genres")
public class Genre {

    @Id
    private final Long id;

    @NotNull
    private final String name;

    @PersistenceCreator
    public Genre(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }
}
