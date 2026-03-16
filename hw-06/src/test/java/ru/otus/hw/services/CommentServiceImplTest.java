package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Проверка работы CommentServiceImpl")
public class CommentServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    private CommentServiceImpl bookService;

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при поиске по id")
    void doesNotThrowExceptionForFindByIdTest() {
        // given
        // when
        Optional<Comment> book = bookService.findById(1);

        // then
        assertThat(book).isPresent();

        Comment expectedComment = book.get();

        assertDoesNotThrow(() -> expectedComment.getBook().getId());
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при поиске всех комментариев по id книги")
    void doesNotThrowExceptionForFindAllTest() {
        // given
        // when
        List<Comment> expectedComments = bookService.findAllByBookId(1);

        // then
        assertDoesNotThrow(() -> expectedComments.get(0).getBook().getId());
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при сохранении комментария")
    void doesNotThrowExceptionForSaveTest() {
        // given
        // when
        Comment expectedComment = bookService.insert("new Comment", 1);

        // then
        assertDoesNotThrow(() -> expectedComment.getBook().getId());
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при изменении комментария")
    void doesNotThrowExceptionForUpdateTest() {
        // given
        // when
        Comment expectedComment = bookService.update(1, "update Book", 2);

        // then
        assertDoesNotThrow(() -> expectedComment.getBook().getId());
    }
}
