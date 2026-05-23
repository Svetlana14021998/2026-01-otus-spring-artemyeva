package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.GenreDocument;

import java.util.List;

@Component
public class Book2BookDocumentConverter {

    public BookDocument convert(Book book, String id, AuthorDocument authorDocument,
        List<GenreDocument> genreDocuments) {
        return BookDocument.builder()
            .id(id)
            .title(book.getTitle())
            .author(authorDocument)
            .genres(genreDocuments)
            .build();
    }
}
