package org.example.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Используется для регистрации
 */
@Data
@Builder
public class UserShortDto {

    @NotBlank(message = "{validation.user.login.required}")
    private String username;

    @Size(min = 5, message = "{validation.user.password.incorrect}")
    private String password;

    @NotBlank(message = "{validation.user.confirm.password.required}")
    private String confirmPassword;

    @NotBlank(message = "{validation.user.confirm.email.required}")
    private String email;
}
