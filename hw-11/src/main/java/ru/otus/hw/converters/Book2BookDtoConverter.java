package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Book2BookDtoConverter {

    private final Genre2GenreDtoConverter genreDtoConverter;

    private final Author2AuthorDtoConverter authorDtoConverter;

    public BookDto convert(Book book, Author author, List<Genre> genres) {
        if (book == null) {
            return null;
        }
        List<GenreDto> genresDto = genres.stream()
            .map(genreDtoConverter::convert)
            .toList();
        return new BookDto(book.getId(), book.getTitle(),
            authorDtoConverter.convert(author), genresDto);
    }
}
