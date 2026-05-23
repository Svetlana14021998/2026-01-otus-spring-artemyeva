package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;

@Component
public class Comment2CommentDocumentConverter {

    public CommentDocument convert(Comment comment, String id, BookDocument bookDocument) {
        return CommentDocument.builder()
            .id(id)
            .text(comment.getText())
            .book(bookDocument)
            .build();
    }
}
