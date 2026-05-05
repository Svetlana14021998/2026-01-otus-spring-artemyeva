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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/api/books")
    public Flux<BookDto> getBooks() {
        return bookService.findAll();
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<?>> deleteBook(@PathVariable Long id) {
        return bookService.deleteById(id)
            .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("/api/books/{id}/comments")
    public Flux<CommentDto> getCommentForBook(@PathVariable Long id) {
        return commentService.findAllByBookId(id);
    }

    @PostMapping("/api/books")
    public ResponseEntity<Mono<BookDto>> createBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.insert(bookDto));
    }

    @PutMapping("/api/books")
    public ResponseEntity<Mono<BookDto>> updateBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.update(bookDto));
    }

    @GetMapping("/api/books/{id}")
    public Mono<BookDto> getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }
}
