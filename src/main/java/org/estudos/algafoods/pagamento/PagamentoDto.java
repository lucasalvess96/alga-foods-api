package org.estudos.algafoods.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Representa uma forma de pagamento no sistema")
public record PagamentoDto(
        @Schema(description = "ID da forma de pagamento", example = "1")
        Long id,

        @Schema(description = "Descrição da forma de pagamento", example = "Cartão de crédito")
        @NotBlank
        @Size(max = 255, message = "A descrição da forma de pagamento deve ter no máximo 255 caracteres")
        String descricao
) {
}
