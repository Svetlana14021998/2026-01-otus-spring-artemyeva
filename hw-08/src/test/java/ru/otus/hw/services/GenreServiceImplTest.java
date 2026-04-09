package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка работы GenreServiceImpl")
class GenreServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    private GenreServiceImpl genreService;

    @Test
    @DisplayName("Поиск всех жанров")
    void findAllTest() {
        //given
        //when
        List<GenreDto> genres = genreService.findAll();

        //then
        assertThat(genres).hasSize(3);
    }
}