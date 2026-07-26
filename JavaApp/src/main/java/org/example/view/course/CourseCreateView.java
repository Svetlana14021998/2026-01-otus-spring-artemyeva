package org.example.view.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;

import java.util.List;

@Data
@Builder
public class CourseCreateView {

    @NotNull(message = "Нужно выбрать тип курса")
    private CourseType courseType;

    @NotBlank(message = "Укажите описание курса")
    private String description;

    @NotEmpty(message = "Должна быть добавлена хотя бы одна страница с материалом")
    private List<String> theoryMaterials;
}
