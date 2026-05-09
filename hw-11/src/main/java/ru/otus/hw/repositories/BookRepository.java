package ru.otus.hw.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

    Mono<Book> findById(long id);

    Flux<Book> findAll();

    @Query("select genre_id from books_genres where book_id = :bookId")
    Flux<Long> findGenreIdsByBookId(Long bookId);

    @Query("delete from books_genres where book_id = :bookId")
    Mono<Void> deleteBookGenresByBookId(Long bookId);

    @Query("insert into books_genres(book_id, genre_id) values (:bookId, :genreId)")
    Mono<Void> addGenreToBook(Long bookId, Long genreId);
}
