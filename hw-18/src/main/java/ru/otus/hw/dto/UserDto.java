package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "{username-must-be-not-blank}")
    @Size(min = 2, max = 50, message = "{username-size}")
    private String username;

    @NotBlank(message = "{password-must-be-not-blank}")
    @Size(min = 4, max = 72, message = "{password-size}")
    private String password;
}
