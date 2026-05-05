package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {

    Mono<Comment> findById(long id);

    Flux<Comment> findAllByBookId(long id);
}
