package org.example.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 80, message = "El nombre no puede exceder 80 caracteres.")
        String name,

        @NotBlank(message = "El slug es obligatorio.")
        @Size(max = 80, message = "El slug no puede exceder 80 caracteres.")
        String slug,

        @Size(max = 300, message = "La descripcion no puede exceder 300 caracteres.")
        String description
) {
}
