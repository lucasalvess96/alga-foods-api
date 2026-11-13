package org.estudos.algafoods.restaurante;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.estudos.algafoods.cozinha.CozinhaDto;
import org.estudos.algafoods.endereco.EnderecoDto;
import org.estudos.algafoods.produto.ProdutoDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Representa um restaurante no sistema")
public record RestauranteDto(
        @Schema(description = "ID do restaurante", example = "1")
        Long id,

        @Schema(description = "Nome do restaurante", example = "Restaurante Italiano")
        @NotBlank
        @Size(max = 255, message = "O nome do restaurante deve ter no máximo 255 caracteres")
        String nome,

        @Schema(description = "Taxa de frete do restaurante", example = "10.50")
        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 8, fraction = 2, message = "A taxa de frete deve ter no máximo 8 dígitos inteiros e 2 dígitos decimais")
        BigDecimal taxaFrete,

        @Schema(description = "Indica se o restaurante está ativo", example = "true")
        @NotNull
        Boolean ativo,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Schema(description = "Data de cadastro do restaurante", example = "01/01/2023 10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime dataCadastro,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Schema(description = "Data da última atualização do restaurante", example = "02/01/2023 11:30:00", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime dataAtualizacao,

        @Schema(description = "Cozinha do restaurante")
        @NotNull
        @Valid
        CozinhaDto cozinha,

        @Schema(description = "Endereço do restaurante")
        @NotNull
        @Valid
        EnderecoDto endereco,

        @Schema(description = "Lista de produtos do restaurante")
        @NotNull
        @Valid
        List<ProdutoDto> produtos,

        List<@NotNull @Positive Long> formasPagamento,

        List<@NotNull @Positive Long> responsaveis
) {
}
