package ru.otus.hw.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.BooksGenres;

public interface BooksGenresRepository extends ReactiveCrudRepository<BooksGenres, Long> {

    @Query("select genre_id from books_genres where book_id = :bookId")
    Flux<Long> findGenreIdsByBookId(Long bookId);

    @Query("delete from books_genres where book_id = :bookId")
    Mono<Void> deleteByBookId(Long bookId);
}
