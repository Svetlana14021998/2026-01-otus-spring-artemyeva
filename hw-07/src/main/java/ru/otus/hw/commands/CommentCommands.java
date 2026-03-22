package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    //ac 1
    @ShellMethod(value = "Find all comment for book", key = "ac")
    public String findAllComments(long bookId) {
        var allComments = commentService.findAllByBookId(bookId);
        if (allComments.isEmpty()) {
            return "Not found comments for book with id: %s".formatted(bookId);
        }
        return allComments.stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cfid")
    public String findCommentById(long id) {
        return commentService.findById(id)
            .map(commentConverter::commentToString)
            .orElse("Comment with id %d not found".formatted(id));
    }

    // cins newComment 1
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String text, long bookId) {
        var savedComment = commentService.insert(text, bookId);
        return commentConverter.commentToString(savedComment);
    }

    // cupd 4 newComment 3
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String text, long bookId) {
        var savedComment = commentService.update(id, text, bookId);
        return commentConverter.commentToString(savedComment);
    }

    // cdel 4
    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
