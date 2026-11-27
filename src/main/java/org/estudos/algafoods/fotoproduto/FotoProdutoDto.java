package org.estudos.algafoods.fotoproduto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record FotoProdutoDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(description = "Identificador da foto", example = "1")
        Long id,

        @Schema(description = "Identificador do produto vinculado", example = "2")
        Long produtoId,


        @Schema(description = "Nome original do arquivo", example = "produto.png")
        String nome,

        @Schema(description = "Descricao da foto", example = "Prato servido no restaurante", nullable = true)
        String descricao,

        @Schema(description = "Tipo de conteudo da imagem", example = "image/png")
        String contentType,

        @Schema(description = "Tamanho do arquivo em bytes", example = "102400")
        Long tamanho
) {
}
