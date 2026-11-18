package org.estudos.algafoods.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoDto(
        @Schema(description = "ID do produto", example = "1")
        @NotBlank
        @Size(min = 1, max = 100, message = "O nome do produto deve ter entre 1 e 100 caracteres")
        String nome,

        @Schema(description = "Descrição do produto", example = "Pizza de pepperoni")
        @NotBlank
        @Size(min = 1, max = 255, message = "A descrição do produto deve ter entre 1 e 255 caracteres")
        String descricao,

        @Schema(description = "Preço do produto", example = "29.90")
        @NotNull
        @DecimalMin(value = "0.00", message = "O preço do produto deve ser um valor positivo")
        BigDecimal preco,

        @Schema(description = "Indica se o produto está ativo", example = "true")
        @NotNull
        Boolean ativo
) {
}
