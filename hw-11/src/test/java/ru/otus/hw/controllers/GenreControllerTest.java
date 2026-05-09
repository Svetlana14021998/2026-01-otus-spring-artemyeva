package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
@DisplayName("Проверка работы GenreController")
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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

        when(genreService.findAll()).thenReturn(Flux.fromIterable(genres));

        //when
        webTestClient.get()
            .uri("/api/genres")
            .exchange()

            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(genres));
    }
}