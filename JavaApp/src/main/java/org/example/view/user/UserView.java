package org.example.view.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Используется для просмотра и редактирования профиля
 */
@Data
@Builder
public class UserView {

    private Long id;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Pattern(
        regexp = "^[А-Яа-яA-Za-z]+\\s[А-Яа-яA-Za-z]+$",
        message = "Имя должно состоять из двух слов (фамилия и имя)")
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String pathToPhoto;

    @NotBlank(message = "Email обязателен")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Введите корректный email адрес")
    private String email;

    @Pattern(
        regexp = "^(\\+7|8)[0-9]{10}$",
        message = "Введите корректный номер телефона")
    private String phone;

    private String role;

    private boolean isActive;
}
