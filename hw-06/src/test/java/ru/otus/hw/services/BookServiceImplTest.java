package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Проверка работы BookServiceImpl")
public class BookServiceImplTest extends AbstractServiceImplTest{

    @Autowired
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при поиске по id")
    void doesNotThrowExceptionForFindByIdTest() {
        // given
        // when
        Optional<Book> book = bookService.findById(1);

        // then
        assertThat(book).isPresent();

        Book expectedBook = book.get();

        assertAll(
            () -> assertDoesNotThrow(() -> expectedBook.getAuthor().getId()),
            () -> assertDoesNotThrow(() -> expectedBook.getGenres().size()));
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при поиске всех книг")
    void doesNotThrowExceptionForFindAllTest() {
        // given
        // when
        List<Book> books = bookService.findAll();

        // then
        assertAll(
            () -> assertDoesNotThrow(() -> books.get(0).getAuthor().getId()),
            () -> assertDoesNotThrow(() -> books.get(0).getGenres().size()));
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при сохранении книги")
    void doesNotThrowExceptionForSaveTest() {
        // given
        // when
        Book book = bookService.insert("new Book", 1, Set.of(1L, 2L));

        // then
        assertAll(
            () -> assertDoesNotThrow(() -> book.getAuthor().getId()),
            () -> assertDoesNotThrow(() -> book.getGenres().size()));
    }

    @Test
    @DisplayName("Проверка загрузки всех связанных сущностей при изменении книги")
    void doesNotThrowExceptionForUpdateTest() {
        // given
        // when
        Book book = bookService.update(1, "update Book", 2, Set.of(3L, 4L));

        // then
        assertAll(
            () -> assertDoesNotThrow(() -> book.getAuthor().getId()),
            () -> assertDoesNotThrow(() -> book.getGenres().size()));
    }
}
