package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
@DisplayName("Проверка работы AuthorController")
class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Получение всех авторов")
    void findAllTest() throws Exception {
        //given
        List<AuthorDto> authors = List.of(
            new AuthorDto(1L, "Author1"),
            new AuthorDto(2L, "Author2"),
            new AuthorDto(3L, "Author3"));

        when(authorService.findAll()).thenReturn(Flux.fromIterable(authors));

        //when
        webTestClient.get()
            .uri("/api/authors")
            .exchange()
            //then
            .expectStatus().isOk()
            .expectBody()
            .json(mapper.writeValueAsString(authors));
    }
}