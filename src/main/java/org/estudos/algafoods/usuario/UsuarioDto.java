package org.estudos.algafoods.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank
        @Size(max = 255, message = "O nome do usuário deve ter no máximo 255 caracteres")
        String nome,

        @NotBlank
        @Email
        @Size(max = 255, message = "O email do usuário deve ter no máximo 255 caracteres")
        String email
) {
}
