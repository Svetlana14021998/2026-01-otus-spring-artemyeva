package org.example.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InactiveUserException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        String errorMessage = "❌ Неверный логин или пароль";

        Throwable cause = exception.getCause();
        if (cause instanceof InactiveUserException) {
            log.error("Профиль деактивирован");
            errorMessage = "❌ Ваша учетная запись деактивирована. Обратитесь к администратору.";
        } else {
            log.error("Неверный логин и/или пароль");
        }
        request.getSession().setAttribute("error", errorMessage);
        getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
    }
}
