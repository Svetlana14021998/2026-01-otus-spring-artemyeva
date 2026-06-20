package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.Book2BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("Проверка безопасности методов BookService")
@Sql(scripts = {"/db/delete-all-from-tables.sql", "/db/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookServiceSecurityTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private Book2BookDtoConverter converter;

    @Autowired
    private BookRepository bookRepository;

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Получение книги пользователем, у которого есть права")
    void getByIdWhenExistsPermissionTest() {
        //given
        //when
        //then
        assertThatNoException().isThrownBy(() -> bookService.findById(1));
    }

    @WithMockUser(username = "user2")
    @Test
    @DisplayName("Получение книги пользователем, у которого нет прав")
    void getByIdWhenNotExistsPermissionTest() {
        //given
        //when
        assertThatThrownBy(() -> bookService.findById(1))
            //then
            .isInstanceOf(AccessDeniedException.class);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Получение всех книг пользователем, у которого есть права на часть книг")
    void getAllBookByUserWithPermissionToPartialOfBooksTest() {
        //given
        BookDto expectedBook = converter.convert(bookRepository.findById(1)
            .orElseThrow(() -> new EntityNotFoundException("Book with id=1 not found")));

        //when
        List<BookDto> actualBooks = bookService.findAll();

        //then
        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator().containsExactly(expectedBook);
    }

    @WithMockUser(username = "user2")
    @Test
    @DisplayName("Получение всех книг пользователем, у которого нет прав ни на одну книгу")
    void getAllBookByUserWithoutPermissionTest() {
        //given
        //when
        List<BookDto> actualBooks = bookService.findAll();

        //then
        assertThat(actualBooks).isEmpty();
    }

    @Transactional
    @WithMockUser(roles = "ADMIN")
    @Test
    @DisplayName("Получение всех книг пользователем, у которого есть права на все книги")
    void getAllBookByUserWithPermissionToAllBooksTest() {
        //given
        List<BookDto> expectedBooks = bookRepository.findAll().stream().map(converter::convert).toList();

        //when
        List<BookDto> actualBooks = bookService.findAll();

        //then
        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expectedBooks);
    }
}
