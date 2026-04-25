package org.example.comment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotNull(message = "La publicacion es obligatoria.")
        Long postId,

        @NotBlank(message = "El autor es obligatorio.")
        @Size(max = 100, message = "El autor no puede exceder 100 caracteres.")
        String authorName,

        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El correo no es valido.")
        @Size(max = 120, message = "El correo no puede exceder 120 caracteres.")
        String authorEmail,

        @NotBlank(message = "El contenido es obligatorio.")
        @Size(min = 5, max = 1000, message = "El contenido debe tener entre 5 y 1000 caracteres.")
        String content
) {
}
