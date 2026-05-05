package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.Book2BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BooksGenres;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryCustom;
import ru.otus.hw.repositories.BooksGenresRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final Book2BookDtoConverter converter;

    private final BooksGenresRepository booksGenresRepository;

    private final BookRepositoryCustom bookRepositoryCustom;

    @Transactional(readOnly = true)
    @Override
    public Mono<BookDto> findById(long id) {
        return bookRepository.findById(id)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Books with id=%d not found!".formatted(id))))
            .flatMap(book ->
                authorRepository.findById(book.getAuthorId())
                    .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        "Author with id=%d not found for book id=%d".formatted(book.getAuthorId(), book.getId()))))
                    .flatMap(author ->
                        booksGenresRepository.findGenreIdsByBookId(book.getId())
                            .collectList()
                            .flatMap(genreIds ->
                                genreRepository.findAllByIdIn(genreIds)
                                    .collectList()
                                    .map(genres -> converter.convert(book, author, genres)))));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<BookDto> findAll() {
        return bookRepositoryCustom.findAll();
    }

    @Transactional
    @Override
    public Mono<BookDto> insert(BookDto bookDto) {
        return save(bookDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> update(BookDto bookDto) {
        return save(bookDto);
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(long id) {
        return bookRepository.findById(id)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Books with id=%d not found!".formatted(id))))
            .flatMap(book ->
                booksGenresRepository.deleteByBookId(id)
                    .then(bookRepository.deleteById(id)));
    }

    private Mono<BookDto> save(BookDto bookDto) {
        long authorId = bookDto.getAuthor().getId();
        List<Long> genresIds = bookDto.getGenres().stream()
            .map(GenreDto::getId)
            .toList();
        return authorRepository.findById(authorId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found: " + authorId)))
            .flatMap(author -> genreRepository.findAllByIdIn(genresIds)
                .collectList()
                .flatMap(genres -> {
                    if (genres.size() != genresIds.size()) {
                        return Mono.error(new EntityNotFoundException("One or all genres with ids %s not found"
                            .formatted(genresIds)));
                    }
                    var book = new Book(bookDto.getId(), authorId, bookDto.getTitle());
                    return saveOrUpdateBook(book, author, genres);
                }));
    }

    private Mono<BookDto> saveOrUpdateBook(Book book, Author author, List<Genre> genres) {
        boolean isNewBook = book.getId() == null;
        List<Long> genreIds = genres.stream().map(Genre::getId).toList();
        return bookRepository.save(book)
            .flatMap(savedBook -> (isNewBook ? saveBookGenres(savedBook.getId(), genreIds)
                : updateBookGenres(savedBook.getId(), genreIds))
                .thenReturn(savedBook))
            .map(savedBook -> converter.convert(savedBook, author, genres));
    }

    private Mono<Void> saveBookGenres(Long bookId, List<Long> genreIds) {
        return booksGenresRepository.saveAll(genreIds.stream()
            .map(genreId -> new BooksGenres(bookId, genreId))
            .toList()).then();
    }

    private Mono<Void> updateBookGenres(Long bookId, List<Long> newGenreIds) {
        return booksGenresRepository.deleteByBookId(bookId)
            .then(saveBookGenres(bookId, newGenreIds));
    }
}