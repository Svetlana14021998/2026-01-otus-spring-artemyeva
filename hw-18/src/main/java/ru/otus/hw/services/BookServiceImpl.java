package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.Book2BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.security.BookAclService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final Book2BookDtoConverter converter;

    private final BookAclService aclServiceWrapperService;

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findByIdFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @PreAuthorize("canRead(#id, T(ru.otus.hw.models.Book))")
    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        log.info("find book by id {}", id);
        Optional<Book> book = bookRepository.findById(id);
        return converter.convert(book
            .orElseThrow(() -> new EntityNotFoundException("Books with id=%d not found!".formatted(id))));
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "findAllFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @PostFilter("hasPermission(filterObject.getId(), 'ru.otus.hw.models.Book', 'READ')")
    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        log.info("find all books");
        List<Book> books = bookRepository.findAll();
        return new ArrayList<>(books.stream()
            .map(converter::convert)
            .toList());
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "insertFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        log.info("create new book");
        Book book = save(bookDto);
        aclServiceWrapperService.createPermission(book, BasePermission.READ);
        return converter.convert(book);
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "updateFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        log.info("update book with id {}", bookDto.getId());
        return converter.convert(save(bookDto));
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "deleteFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Override
    @Transactional
    public void deleteById(long id) {
        log.info("delete book with id {}", id);
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Books with id=%d not found!".formatted(id)));
        bookRepository.delete(book);
        aclServiceWrapperService.deletePermissions(book);
    }

    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "hasAnyBookFallback")
    @RateLimiter(name = "defaultRateLimiter")
    @Override
    public boolean hasAnyBook() {
        return bookRepository.hasAnyBook();
    }

    private Book save(BookDto bookDto) {
        long authorId = bookDto.getAuthor().getId();
        var author = authorRepository.findById(authorId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        Set<Long> genresIds = bookDto.getGenres().stream()
            .map(GenreDto::getId)
            .collect(Collectors.toSet());
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(bookDto.getId(), bookDto.getTitle(), author, genres);
        return bookRepository.save(book);
    }

    public BookDto findByIdFallback(long id, Throwable ex) {
        return null;
    }

    public List<BookDto> findAllFallback(Throwable ex) {
        return List.of();
    }

    public BookDto insertFallback(BookDto bookDto, Throwable ex) {
        return null;
    }

    public BookDto updateFallback(BookDto bookDto, Throwable ex) {
        return null;
    }

    public void deleteFallback(long id, Throwable ex) {
    }

    public boolean hasAnyBookFallback(Throwable ex) {
        return false;
    }
}
