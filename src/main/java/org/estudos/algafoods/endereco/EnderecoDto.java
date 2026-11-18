package org.estudos.algafoods.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDto(
        @Size(max = 8)
        @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
        @Schema(description = "cep", example = "66000000", nullable = true)
        String cep,

        @NotBlank
        @Size(max = 255, message = "O campo logradouro deve conter no máximo 255 caracteres")
        @Schema(description = "logradouro", example = "Avenida Presidente Vargas")
        String logradouro,

        @NotBlank
        @Size(max = 10, message = "O campo numero deve conter no máximo 10 caracteres")
        @Schema(description = "numero", example = "100")
        String numero,

        @Size(max = 100, message = "O campo complemento deve conter no máximo 100 caracteres")
        @Schema(description = "complemento", example = "Apartamento 101", nullable = true)
        String complemento,

        @NotBlank
        @Size(max = 100, message = "O campo bairro deve conter no máximo 100 caracteres")
        @Schema(description = "bairro", example = "Campina")
        String bairro,

        @NotBlank
        @Size(max = 100, message = "O campo município deve conter no máximo 100 caracteres")
        @Schema(description = "municipio", example = "Belém")
        String cidade,

        @NotBlank
        @Size(max = 2, message = "O campo UF deve conter apenas 2 caracteres")
        @Schema(description = "uf", example = "PA")
        String estado
) {
}
