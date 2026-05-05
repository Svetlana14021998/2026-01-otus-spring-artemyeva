package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

    Mono<Book> findById(long id);

    Flux<Book> findAll();
}
