package org.example.dto.user;

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
public class UserDto {

    private Long id;

    @NotBlank(message = "{validation.user.fullName.required}")
    @Pattern(
        regexp = "^[А-Яа-яA-Za-z]+\\s[А-Яа-яA-Za-z]+$",
        message = "{validation.user.fullName.incorrect}")
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String pathToPhoto;

    @NotBlank(message = "{validation.user.email.required}")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "{validation.user.email.incorrect}")
    private String email;

    @Pattern(
        regexp = "^(\\+7|8)[0-9]{10}$",
        message = "{validation.user.phone.incorrect}")
    private String phone;

    private String role;

    private boolean isActive;
}
