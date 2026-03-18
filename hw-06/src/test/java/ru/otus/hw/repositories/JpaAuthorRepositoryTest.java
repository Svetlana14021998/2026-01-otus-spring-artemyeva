package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = "/db/author-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import(JpaAuthorRepository.class)
@DisplayName("Проверка работы JdbcAuthorRepository")
public class JpaAuthorRepositoryTest extends AbstractRepositoryTest {

    private static final int AUTHORS_COUNT = 3;

    private static final long FIRST_AUTHOR_ID = 1;

    @Autowired
    private JpaAuthorRepository repository;

    @Test
    @DisplayName("Поиск автора по его id")
    void findAuthorByIdTest() {
        //given
        //when
        Optional<Author> author = repository.findById(FIRST_AUTHOR_ID);

        //then
        Author expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

        assertThat(author).isPresent();
        assertThat(author.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    @DisplayName("Поиск всех авторов")
    void findAllAuthorsTest() {
        //given
        //when
        List<Author> authors = repository.findAll();

        //then
        assertThat(authors).isNotNull().hasSize(AUTHORS_COUNT);
    }
}
