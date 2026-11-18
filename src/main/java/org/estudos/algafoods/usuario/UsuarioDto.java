package org.estudos.algafoods.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        @Schema(description = "ID do usuário", example = "1")
        Long id,

        @Schema(description = "Nome do usuário", example = "MARIA SILVA")
        @NotBlank
        @Size(max = 255, message = "O nome do usuário deve ter no máximo 255 caracteres")
        String nome,

        @Schema(description = "Email do usuário", example = "maria@email.com")
        @NotBlank
        @Email
        @Size(max = 255, message = "O email do usuário deve ter no máximo 255 caracteres")
        String email
) {
}
