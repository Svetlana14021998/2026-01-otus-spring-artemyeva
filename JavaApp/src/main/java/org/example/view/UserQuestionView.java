package org.example.view;

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
public class UserQuestionView {

    @NotBlank(message = "Имя - обязательно для заполнения")
    private String name;

    @NotBlank(message = "Почта должна быть заполнена")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$|^$",
        message = "Почта имеет некорретный формат")
    private String email;

    @NotBlank(message = "Введите ваш вопрос")
    private String message;

    private Long id;
    private LocalDateTime createAt;
}
