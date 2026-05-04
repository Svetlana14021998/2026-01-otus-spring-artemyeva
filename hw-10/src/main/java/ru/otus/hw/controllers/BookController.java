package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/api/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/books/{id}/comments")
    public List<CommentDto> getCommentForBook(@PathVariable Long id) {
        return commentService.findAllByBookId(id);
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        BookDto saveBook = bookService.insert(bookDto);
        return ResponseEntity.ok(saveBook);
    }

    @PutMapping("/api/books")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        BookDto updateBook = bookService.update(bookDto);
        return ResponseEntity.ok(updateBook);
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }
}
