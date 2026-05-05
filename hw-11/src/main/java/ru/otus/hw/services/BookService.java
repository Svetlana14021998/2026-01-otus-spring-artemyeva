package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;

public interface BookService {

    Mono<BookDto> findById(long id);

    Flux<BookDto> findAll();

    Mono<BookDto> insert(BookDto bookDto);

    Mono<BookDto> update(BookDto bookDto);

    Mono<Void> deleteById(long id);
}
