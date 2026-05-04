package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@DisplayName("Проверка работы AuthorController")
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthorService authorService;

    @Test
    @DisplayName("Получение всех авторов")
    void findAllTest() throws Exception {
        //given
        List<AuthorDto> authors = List.of(
            new AuthorDto(1, "Author1"),
            new AuthorDto(2, "Author2"),
            new AuthorDto(3, "Author3"));

        when(authorService.findAll()).thenReturn(authors);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/authors"))
            //then
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(authors)));
    }
}