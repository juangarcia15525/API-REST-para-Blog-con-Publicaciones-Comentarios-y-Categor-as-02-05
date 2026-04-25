package org.example.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PostRequest(
        @NotBlank(message = "El titulo es obligatorio.")
        @Size(max = 150, message = "El titulo no puede exceder 150 caracteres.")
        String title,

        @NotBlank(message = "El slug es obligatorio.")
        @Size(max = 100, message = "El slug no puede exceder 100 caracteres.")
        String slug,

        @NotBlank(message = "El contenido es obligatorio.")
        @Size(min = 20, max = 5000, message = "El contenido debe tener entre 20 y 5000 caracteres.")
        String content,

        @NotEmpty(message = "Debe incluir al menos una categoria.")
        List<Long> categoryIds
) {
}
