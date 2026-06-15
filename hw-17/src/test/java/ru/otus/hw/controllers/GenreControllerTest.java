package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("Проверка работы GenreController")
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private GenreService genreService;

    @Test
    @DisplayName("Получение всех жанров")
    void findAllTest() throws Exception {
        //given
        List<GenreDto> genres = List.of(
            new GenreDto(1L, "Genre1"),
            new GenreDto(2L, "Genre2"),
            new GenreDto(3L, "Genre3"));

        when(genreService.findAll()).thenReturn(genres);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/genres"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(genres)));
    }
}