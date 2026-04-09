package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка работы AuthorServiceImpl")
class AuthorServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @Test
    @DisplayName("Поиск всех авторов")
    void findAllTest() {
        //given
        //when
        List<AuthorDto> authors = authorService.findAll();

        //then
        assertThat(authors).hasSize(2);
    }
}