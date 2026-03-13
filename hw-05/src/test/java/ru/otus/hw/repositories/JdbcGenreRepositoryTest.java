package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import({JdbcGenreRepository.class})
@Sql(scripts = "/db/genre-data.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repository;

    @Test
    @DisplayName("Получение всех жанров")
    void getCorrectGenreListTest() {
        //given
        List<Genre> expectedGenres = allGenres();

        //when
        List<Genre> actualGenres = repository.findAll();

        //then
        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    private static Stream<Arguments> genreIds() {
        return Stream.of(
            Arguments.of(Set.of(1L), "Передан один id"),
            Arguments.of(Set.of(1L, 3L), "Передано несколько id")
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Получение жанров по id")
    @MethodSource("genreIds")
    void getGenresByIdTest(Set<Long> ids,String description) {
        //given
        List<Genre> expectedGenres = allGenres().stream()
            .filter(genre -> ids.contains(genre.getId()))
            .toList();

        //when
        List<Genre> actualGenres = repository.findAllByIds(ids);

        //then
        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    private static List<Genre> allGenres() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Genre(id, "Genre_" + id))
            .toList();
    }
}
