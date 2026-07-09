package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.LoginAlreadyExistsException;
import ru.otus.hw.exceptions.RoleUserNotExistsInDBException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handeNotFoundException(EntityNotFoundException ex) {
        String text = messageSource.getMessage("entity-not-found-error", null,
            LocaleContextHolder.getLocale());
        return new ModelAndView("error", "errorText", text);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation Failed");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> hangleSQLException(LoginAlreadyExistsException e) {
        Map<String, Object> response = new HashMap<>();
        String message = messageSource.getMessage("login_already_used", null,
            LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("login_already_exists", null,
            LocaleContextHolder.getLocale());
        response.put("message", message);
        response.put("login_already_exists", error);
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(response);
    }

    @ExceptionHandler(RoleUserNotExistsInDBException.class)
    public ResponseEntity<Map<String, Object>> hangleRoleUserNotExistsInDBException(RoleUserNotExistsInDBException e) {
        Map<String, Object> response = new HashMap<>();
        String message = messageSource.getMessage("server-error", null,
            LocaleContextHolder.getLocale());
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(response);
    }
}
