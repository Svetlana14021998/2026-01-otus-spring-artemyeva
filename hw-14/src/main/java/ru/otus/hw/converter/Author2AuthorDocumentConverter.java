package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.mongo.AuthorDocument;

@Component
public class Author2AuthorDocumentConverter {

    public AuthorDocument convert(Author author, String id) {
        return AuthorDocument.builder()
            .id(id)
            .fullName(author.getFullName())
            .build();
    }
}
