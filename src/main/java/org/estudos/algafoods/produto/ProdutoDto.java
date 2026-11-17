package org.estudos.algafoods.produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoDto(
        @NotBlank String nome,
        String descricao,
        @NotNull @DecimalMin("0.00") BigDecimal preco,
        @NotNull Boolean ativo
) {
}
