package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(scripts = "/db/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({JdbcCommentRepository.class})
@DisplayName("Проверка работы JdbcCommentRepository")
class JdbcCommentRepositoryTest extends AbstractRepositoryTest {

    private static final long FIRST_COMMENT_ID = 1;

    @Autowired
    private JdbcCommentRepository repository;

    @Test
    @DisplayName("Поиск комментария по его id")
    void findCommentByIdTest() {
        //given
        //when
        var comment = repository.findById(FIRST_COMMENT_ID);

        //then
        var expectedComment = em.find(Comment.class, FIRST_COMMENT_ID);

        assertThat(comment).isPresent();
        var actualComment = comment.get();
        assertAll(
            () -> assertThat(actualComment.getText()).isEqualTo(expectedComment.getText()),
            () -> assertThat(actualComment.getId()).isEqualTo(expectedComment.getId()),
            () -> assertThat(actualComment.getBook().getId()).isEqualTo(expectedComment.getBook().getId()));
    }

    @Test
    @DisplayName("Поиск комментариев по id книги")
    void findAllCommentsByBookIdIdTest() {
        //given
        //when
        var comments = repository.findAllByBookId(2);

        //then
        assertThat(comments).isNotEmpty()
            .allMatch(comment -> !comment.getText().isEmpty())
            .allMatch(comment -> comment.getBook() != null);
    }

    @Test
    @DisplayName("Проверка сохранения комментария")
    void saveCommentTest() {
        //given
        Book book = em.find(Book.class, 3);
        Comment comment = new Comment(0, "newComment", book);

        //when
        var savedComment = repository.save(comment);
        em.flush();
        em.clear();

        //then
        assertThat(savedComment.getId()).isPositive();

        Comment commentFromDB = em.find(Comment.class, comment.getId());

        assertThat(commentFromDB).isNotNull();
        assertAll(
            () -> assertThat(savedComment.getText()).isEqualTo(commentFromDB.getText()),
            () -> assertThat(savedComment.getBook().getId()).isEqualTo(commentFromDB.getBook().getId()));
    }

    @Test
    @DisplayName("Проверка изменение комментария")
    void updateCommentTest() {
        //given
        var comment = em.find(Comment.class, FIRST_COMMENT_ID);
        String newCommentText = "new Text";
        comment.setText(newCommentText);

        //when
        repository.save(comment);
        em.flush();
        em.clear();

        //then
        Comment commentFromDB = em.find(Comment.class, FIRST_COMMENT_ID);

        assertThat(commentFromDB.getText()).isEqualTo(newCommentText);
    }

    @Test
    @DisplayName("Проверка удаления комментария")
    void deleteCommentTest() {
        //when
        repository.deleteById(FIRST_COMMENT_ID);

        //then
        Comment commentFromDB = em.find(Comment.class, FIRST_COMMENT_ID);

        assertThat(commentFromDB).isNull();
    }
}