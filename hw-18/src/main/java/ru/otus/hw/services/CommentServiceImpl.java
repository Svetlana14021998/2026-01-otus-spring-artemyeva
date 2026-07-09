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
        log.info("find comment by id {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        return Optional.ofNullable(converter.convert(comment.orElse(null)));
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findAllByBookIdFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        log.info("find comments for book with id {}", bookId);
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
        log.info("create new comment for book with id {}", bookId);
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
        log.info("update comment with id {}", id);
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
        log.info("delete comment with id {}", id);
        commentRepository.deleteById(id);
    }

    public Optional<CommentDto> findByIdFallback(long id, Throwable ex) {
        return Optional.empty();
    }

    public List<CommentDto> findAllByBookIdFallback(long bookId, Throwable ex) {
        return new ArrayList<>();
    }

    public CommentDto insertFallback(String text, long bookId, Throwable ex) {
        return null;
    }

    public CommentDto updateFallback(long id, String newText, long newBookId, Throwable ex) {
        return null;
    }

    public void deleteByIdFallback(long id, Throwable ex) {
    }
}
