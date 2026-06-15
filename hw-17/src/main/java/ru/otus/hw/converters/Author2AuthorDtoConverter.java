package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class Author2AuthorDtoConverter {

    public AuthorDto convert(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
