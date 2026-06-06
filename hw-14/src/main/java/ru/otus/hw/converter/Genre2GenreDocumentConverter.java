package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

@Component
public class Genre2GenreDocumentConverter {

    public GenreDocument convert(Genre genre, String id) {
        return GenreDocument.builder()
            .id(id)
            .name(genre.getName())
            .build();
    }
}
