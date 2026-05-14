package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.services.GenreService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(SecurityConfig.class)
@DisplayName("Проверка доступа к методам GenreController в зависимости от аутентификации")
public class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    @DisplayName("Получение всех жанров. Пользователь аутентифицирован")
    void getAllGenresWithAuthenticationTest() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres")
                .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            //then
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение всех жанров. Пользователь не аутентифицирован")
    void getAllGenresWithoutAuthenticationTest() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres"))
            //then
            .andExpect(status().isFound())
            .andExpect(redirectedUrlPattern("**/login"));
    }
}
