package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
public class Comment2CommentDtoConverter {

    public CommentDto convert(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(comment.getId(), comment.getText(), comment.getBookId());
    }
}
