package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookDto {

    private long id;

    @NotBlank(message = "{title-must-be-not-blank}")
    @Size(min = 2, max = 50, message = "{title-size}")
    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    public String genresAsString() {
        if (genres.isEmpty()) {
            return "";
        }
        return genres.stream()
            .map(GenreDto::getName)
            .collect(Collectors.joining(", "));
    }
}
