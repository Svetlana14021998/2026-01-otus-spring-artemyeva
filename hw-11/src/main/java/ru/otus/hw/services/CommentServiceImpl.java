package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.Comment2CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Comment2CommentDtoConverter converter;

    @Transactional(readOnly = true)
    @Override
    public Mono<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(converter::convert);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<CommentDto> findAllByBookId(long bookId) {
        return bookRepository.findById(bookId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id=%d not found!".formatted(bookId))))
            .thenMany(commentRepository.findAllByBookId(bookId))
            .map(converter::convert);
    }

    @Transactional
    @Override
    public Mono<CommentDto> insert(String text, long bookId) {
        return bookRepository.findById(bookId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %d not found".formatted(bookId))))
            .flatMap(book -> commentRepository.save(new Comment(0L, bookId, text)).map(converter::convert));
    }

    @Transactional
    @Override
    public Mono<CommentDto> update(long id, String newText, long newBookId) {
        return bookRepository.findById(newBookId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %d not found".formatted(newBookId))))
            .flatMap(book -> commentRepository.save(new Comment(id, newBookId, newText)))
            .map(converter::convert);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
