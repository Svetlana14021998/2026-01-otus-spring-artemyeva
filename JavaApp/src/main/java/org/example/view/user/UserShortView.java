package org.example.view.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Используется для регистрации
 */
@Data
@Builder
public class UserShortView {

    @NotBlank(message = "Логин - обязательно для заполнения")
    private String username;

    @Size(min = 5, message = "Пароль должен состоять минимум из 5 знаков")
    private String password;

    @NotBlank(message = "Подтвердите введенный пароль")
    private String confirmPassword;

    @NotBlank(message = "Почта - обязательно для заполнения")
    private String email;
}
