package ru.otus.hw.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustom {

    private static final String SQL_ALL = """
        select
            b.id,
            b.title,
            (select json_object(key 'id' value a.id, key 'name' value a.full_name)
             from authors a
             WHERE a.id = b.author_id) as author,
            (select json_arrayagg(json_object(key 'id' value g.id, key 'name' value g.name))
             from books_genres bg
             join genres g ON bg.genre_id = g.id
             where bg.book_id = b.id) as genres
        from books b
        """;

    private final R2dbcEntityTemplate template;

    private final ObjectMapper objectMapper;

    public Flux<BookDto> findAll() {
        return template.getDatabaseClient().inConnectionMany(connection ->
            Flux.from(connection.createStatement(SQL_ALL)
                    .execute())
                .flatMap(result -> result.map(this::mapper)));
    }

    private BookDto mapper(Readable selectedRecord) {
        Long id = selectedRecord.get("id", Long.class);
        String title = selectedRecord.get("title", String.class);

        String authorJson = selectedRecord.get("author", String.class);
        AuthorDto author = parseAuthorDto(authorJson);

        String genresJson = selectedRecord.get("genres", String.class);
        List<GenreDto> genres = parseGenresDto(genresJson);

        return new BookDto(id, title, author, genres);
    }

    private AuthorDto parseAuthorDto(String authorJson) {
        try {
            JsonNode node = objectMapper.readTree(authorJson);
            return new AuthorDto(
                node.get("id").asLong(),
                node.get("name").asText()
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Author JSON parsing error: " + authorJson, e);
        }
    }

    private List<GenreDto> parseGenresDto(String genresJson) {
        try {
            List<GenreDto> genres = objectMapper.readValue(genresJson, new TypeReference<>() {
            });
            return new ArrayList<>(genres);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Genres JSON parsing error: " + genresJson, e);
        }
    }
}
