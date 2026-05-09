package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PageController.class)
@Import(SecurityConfig.class)
@DisplayName("Проверка доступа к страницам в зависимости от аутентификации")
public class PagesSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private static Stream<Arguments> urlWithAuth() {
        return Stream.of(
            Arguments.of("/genres", "Страница жанров"),
            Arguments.of("/authors", "Страница авторов"),
            Arguments.of("/books", "Страница книг"),
            Arguments.of("/books/create", "Страница создания книги"),
            Arguments.of("/books/1/edit", "Страница редактирования книги"),
            Arguments.of("/books/1/comments", "Страница комментариев")
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Переход на страницу, требующую аутентификации. Пользователь аутентифицирован")
    @MethodSource("urlWithAuth")
    void toPageWithAuthWhenAuthenticationTest(String url, String pageName) throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .with(SecurityMockMvcRequestPostProcessors.user("admin")
                    .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            //then
            .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Переход на страницу, требующую аутентификации. Пользователь не аутентифицирован")
    @MethodSource("urlWithAuth")
    void toPageWithAuthWhenNotAuthenticationTest(String url, String pageName) throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            //then
            .andExpect(status().isFound());
    }

    private static Stream<Arguments> urlWithoutAuth() {
        return Stream.of(
            Arguments.of("/main", "Главная страница"),
            Arguments.of("/register", "Страница регистрации"),
            Arguments.of("/login", "Страница входа")
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Переход на страницу, не требующую аутентификации. Пользователь не аутентифицирован")
    @MethodSource("urlWithoutAuth")
    void toPageWithoutAuthWhenNotAuthenticationTest(String url, String pageName) throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            //then
            .andExpect(status().isOk());
    }
}
