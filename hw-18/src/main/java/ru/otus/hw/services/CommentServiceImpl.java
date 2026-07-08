package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.Comment2CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Comment2CommentDtoConverter converter;

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findByIdFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return Optional.ofNullable(converter.convert(comment.orElse(null)));
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findAllByBookIdFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book with id=%d not found!".formatted(bookId));
        }
        return commentRepository.findAllByBookId(bookId).stream()
            .map(converter::convert)
            .toList();
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "insertFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional
    @Override
    public CommentDto insert(String text, long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        Comment comment = commentRepository.save(new Comment(0L, text, book));
        return converter.convert(comment);
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "updateFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional
    @Override
    public CommentDto update(long id, String newText, long newBookId) {
        Book book = bookRepository.findById(newBookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(newBookId)));
        Comment comment = commentRepository.save(new Comment(id, newText, book));
        return converter.convert(comment);
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "deleteByIdFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    public Optional<CommentDto> findByIdFallback(long id, Throwable ex) {
        log.error("Fallback for findById({}) triggered: {}", id, ex.getMessage());
        return Optional.empty();
    }

    public List<CommentDto> findAllByBookIdFallback(long bookId, Throwable ex) {
        log.error("Fallback for findAllByBookId({}) triggered: {}", bookId, ex.getMessage());
        return new ArrayList<>();
    }

    public CommentDto insertFallback(String text, long bookId, Throwable ex) {
        log.error("Fallback for insert(text='{}', bookId={}) triggered: {}", text, bookId, ex.getMessage());
        return null;
    }

    public CommentDto updateFallback(long id, String newText, long newBookId, Throwable ex) {
        log.error("Fallback for update(id={}, newBookId={}) triggered: {}", id, newBookId, ex.getMessage());
        return null;
    }

    public void deleteByIdFallback(long id, Throwable ex) {
        log.error("Fallback for deleteById({}) triggered: {}", id, ex.getMessage());
    }
}
