package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handeNotFoundException(EntityNotFoundException ex) {
        String text = messageSource.getMessage("book-not-found-error", null,
            LocaleContextHolder.getLocale());
        return new ModelAndView("errorPage", "errorText", text);
    }
}
