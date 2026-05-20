package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final Book2BookDtoConverter converter;

    private final BookAclService aclServiceWrapperService;

    @PreAuthorize("canRead(#id, T(ru.otus.hw.models.Book))")
    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return converter.convert(book
            .orElseThrow(() -> new EntityNotFoundException("Books with id=%d not found!".formatted(id))));
    }

    @PostFilter("hasPermission(filterObject.getId(), 'ru.otus.hw.models.Book', 'READ')")
    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
            .map(converter::convert)
            .toList();
    }

    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        Book book = save(bookDto);
        aclServiceWrapperService.createPermission(book, BasePermission.READ);
        return converter.convert(book);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        return converter.convert(save(bookDto));
    }

    @Secured("ROLE_ADMIN")
    @Override
    @Transactional
    public void deleteById(long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Books with id=%d not found!".formatted(id)));
        bookRepository.delete(book);
        aclServiceWrapperService.deletePermissions(book);
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
}
