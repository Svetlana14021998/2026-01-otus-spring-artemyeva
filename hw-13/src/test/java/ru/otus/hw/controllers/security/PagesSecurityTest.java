package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.PageController;

import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PageController.class)
@Import(SecurityConfig.class)
@DisplayName("Проверка доступа к страницам в зависимости от аутентификации")
public class PagesSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private static Stream<Arguments> testData() {
        return Stream.of(
            //Пользователь аутентифицирован
            Arguments.of("/genres", "Страница жанров.Пользователь аутентифицирован", "admin", "ROLE_ADMIN", false, 200),
            Arguments.of("/authors", "Страница авторов.Пользователь аутентифицирован", "admin", "ROLE_ADMIN", false, 200),
            Arguments.of("/books", "Страница книг.Пользователь аутентифицирован", "admin", "ROLE_ADMIN", false, 200),
            Arguments.of("/books/create", "Страница создания книги.Пользователь аутентифицирован", "admin", "ROLE_ADMIN", false, 200),
            Arguments.of("/books/1/edit", "Страница редактирования книги.Пользователь аутентифицирован", "admin", "ROLE_ADMIN", false, 200),
            Arguments.of("/books/1/comments", "Страница комментариев.Пользователь аутентифицирован", "admin", "ROLE_ADMIN", false, 200),
            //Пользователь не аутентифицирован
            Arguments.of("/genres", "Страница жанров.Пользователь  не аутентифицирован", null, null, true, 302),
            Arguments.of("/authors", "Страница авторов.Пользователь не аутентифицирован", null, null, true, 302),
            Arguments.of("/books", "Страница книг.Пользователь не аутентифицирован", null, null, true, 302),
            Arguments.of("/books/create", "Страница создания книги.Пользователь не аутентифицирован", null, null, true, 302),
            Arguments.of("/books/1/edit", "Страница редактирования книги.Пользователь не аутентифицирован", null, null, true, 302),
            Arguments.of("/books/1/comments", "Страница комментариев.Пользователь не аутентифицирован", null, null, true, 302)
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Переход на страницу, требующую аутентификации.")
    @MethodSource("testData")
    void toPageWithAuthTest(String url, String pageName, String userName, String role, boolean checkRedirect, int status)
        throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        if (nonNull(userName)) {
            requestBuilder.with(user(userName)
                .authorities(new SimpleGrantedAuthority(role)));
        }
        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().is(status));

        if (checkRedirect) {
            result.andExpect(redirectedUrlPattern("**/login"));
        }
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
