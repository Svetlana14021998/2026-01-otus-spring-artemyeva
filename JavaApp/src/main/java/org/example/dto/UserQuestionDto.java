package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQuestionDto {

    @NotBlank(message = "{validation.user.fullName.required}")
    private String name;

    @NotBlank(message = "{validation.user.email.required}")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$|^$",
        message = "{validation.user.email.incorrect}")
    private String email;

    @NotBlank(message = "{validation.user.question.message.required}")
    private String message;

    private Long id;
    private LocalDateTime createAt;
}
