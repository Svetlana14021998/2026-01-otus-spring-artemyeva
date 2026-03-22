package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("Проверка работы CommentServiceImpl")
public class CommentServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при поиске по id")
    void doesNotThrowExceptionForFindByIdTest() {
        // given
        // when
        assertThatNoException()
            .isThrownBy(() -> commentService.findById(1));
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при поиске всех комментариев по id книги")
    void doesNotThrowExceptionForFindAllTest() {
        // given
        // when
        assertThatNoException()
            .isThrownBy(() -> commentService.findAllByBookId(1));
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при сохранении комментария")
    void doesNotThrowExceptionForSaveTest() {
        // given
        // when
        assertThatNoException()
            .isThrownBy(() -> commentService.insert("new Comment", 1));
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при изменении комментария")
    void doesNotThrowExceptionForUpdateTest() {
        // given
        // when
        assertThatNoException()
            .isThrownBy(() -> commentService.update(1, "update Book", 2));
    }
}
