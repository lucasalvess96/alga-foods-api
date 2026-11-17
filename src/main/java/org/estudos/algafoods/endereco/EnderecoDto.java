package org.estudos.algafoods.endereco;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDto(
        @NotBlank String cep,
        @NotBlank String logradouro,
        @NotBlank String numero,
        String complemento,
        @NotBlank String bairro
) {
}
