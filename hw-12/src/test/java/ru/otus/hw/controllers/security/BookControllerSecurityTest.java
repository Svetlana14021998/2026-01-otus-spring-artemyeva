package ru.otus.hw.controllers.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
@DisplayName("Проверка доступа к методам BookController в зависимости от аутентификации")
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    private static Stream<Arguments> testData() {
        return Stream.of(
            //Пользователь аутентифицирован
            Arguments.of("get", "/api/books", "admin", "ROLE_ADMIN", 200, false,
                "Получение всех книг. Пользователь аутентифицирован"
            ),
            Arguments.of("delete", "/api/books/1", "admin", "ROLE_ADMIN", 200, false,
                "Удаление книги по id. Пользователь аутентифицирован"
            ),
            Arguments.of("get", "/api/books/1/comments", "admin", "ROLE_ADMIN", 200, false,
                "Получение комментариев по id книги. Пользователь аутентифицирован"
            ),
            Arguments.of("post", "/api/books", "admin", "ROLE_ADMIN", 201, false,
                "Создание книги. Пользователь аутентифицирован"
            ),
            Arguments.of("put", "/api/books", "admin", "ROLE_ADMIN", 200, false,
                "Изменение книги. Пользователь аутентифицирован"
            ),
            Arguments.of("get", "/api/books/1", "admin", "ROLE_ADMIN", 200, false,
                "Получение книги по id. Пользователь аутентифицирован"
            ),

            //Пользователь не аутентифицирован
            Arguments.of("get", "/api/books", null, null, 302, true,
                "Получение всех книг. Пользователь аутентифицирован"
            ),
            Arguments.of("delete", "/api/books/1", null, null, 302, true,
                "Удаление книги по id. Пользователь аутентифицирован"
            ),
            Arguments.of("get", "/api/books/1/comments", null, null, 302, true,
                "Получение комментариев по id книги. Пользователь аутентифицирован"
            ),
            Arguments.of("post", "/api/books", null, null, 302, true,
                "Создание книги. Пользователь аутентифицирован"
            ),
            Arguments.of("put", "/api/books", null, null, 302, true,
                "Изменение книги. Пользователь аутентифицирован"
            ),
            Arguments.of("get", "/api/books/1", null, null, 302, true,
                "Получение книги по id. Пользователь аутентифицирован"
            )
        );
    }

    @ParameterizedTest(name = "{6}")
    @MethodSource("testData")
    void checkAllBookControllerMethodsTest(String method, String url, String userName, String role, int status, boolean checkRedirect,
        String description) throws Exception {
        //given
        var requestBuilder = convertMethodToRequestBuilder(method, url);
        if (nonNull(userName)) {
            requestBuilder = requestBuilder.with(user(userName).authorities(new SimpleGrantedAuthority(role)));
        }

        //when
        var result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().is(status));

        if (checkRedirect) {
            result.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder convertMethodToRequestBuilder(String method, String url) throws JsonProcessingException {
        BookDto bookDto = new BookDto(0, "title", new AuthorDto(1, "author"), List.of(new GenreDto(1, "genre")));
        String body = mapper.writeValueAsString(bookDto);

        Map<String, BiFunction<String, Object, MockHttpServletRequestBuilder>> methods = Map.of(
            "get", (u, b) -> MockMvcRequestBuilders.get(u),
            "post", (u, b) -> MockMvcRequestBuilders.post(u)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
            "put", (u, b) -> MockMvcRequestBuilders.put(u)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
            "delete", MockMvcRequestBuilders::delete
        );
        return methods.get(method).apply(url, body);
    }
}
