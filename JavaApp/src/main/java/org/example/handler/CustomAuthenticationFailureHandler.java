package org.example.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InactiveUserException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        String errorMessage = messageSource.getMessage("authentication.incorrect.login.or.password", null, getLocale());

        Throwable cause = exception.getCause();
        if (cause instanceof InactiveUserException) {
            log.error("Профиль деактивирован");
            errorMessage = messageSource.getMessage("authentication.profile.inactive", null, getLocale());
        } else {
            log.error("Неверный логин и/или пароль");
        }
        request.getSession().setAttribute("error", errorMessage);
        getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
    }
}
