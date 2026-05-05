package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;

public interface CommentService {

    Mono<CommentDto> findById(long id);

    Flux<CommentDto> findAllByBookId(long bookId);

    Mono<CommentDto> insert(String text, long bookId);

    Mono<CommentDto> update(long id, String newText, long newBookId);

    void deleteById(long id);
}
