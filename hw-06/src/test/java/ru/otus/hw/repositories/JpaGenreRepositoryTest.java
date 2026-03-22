package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = "/db/genre-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import(JpaGenreRepository.class)
@DisplayName("Проверка работы JdbcGenreRepository")
class JpaGenreRepositoryTest extends AbstractRepositoryTest {

    private static final int GENRES_COUNT = 4;
    private static final long FIRST_GENRE_ID = 1;
    private static final long THIRD_GENRE_ID = 3;
    @Autowired
    private JpaGenreRepository repository;

    @Test
    @DisplayName("Поиск жанров по списку id-шников")
    void findGenresByIdsTest() {
        //given
        //when
        List<Genre> genres = repository.findAllByIds(Set.of(FIRST_GENRE_ID, THIRD_GENRE_ID));

        //then
        Genre genre1 = em.find(Genre.class, FIRST_GENRE_ID);
        Genre genre3 = em.find(Genre.class, THIRD_GENRE_ID);
        List<Genre> expectedGenres = List.of(genre1, genre3);

        assertThat(genres).isNotEmpty();
        assertThat(genres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @Test
    @DisplayName("Поиск всех авторов")
    void findAllGenresTest() {
        //given
        //when
        List<Genre> genres = repository.findAll();

        //then
        assertThat(genres).hasSize(GENRES_COUNT);
    }
}