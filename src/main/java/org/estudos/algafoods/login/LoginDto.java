package org.estudos.algafoods.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Representa login no sistema")
public record LoginDto(

        @Schema(description = "ID da cozinha", example = "1")
        Long id,

        @Schema(description = "login do usuário", example = "jhon.doe")
        @NotBlank(message = "O campo não pode ser nulo")
        @Size(min = 16, max = 16, message = "O tamanho deve 16 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@$&])[A-Za-z\\d!@$&]+$", message = "O campo NOME deve conter apenas letras")
        String loginUsuario,

        @Schema(description = "Nome da cozinha", example = "Italiana")
        @NotBlank(message = "O campo não pode ser nulo")
        @Size(min = 2, max = 800, message = "O tamanho deve ser entre 2 e 800 caracteres")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "O campo NOME deve conter apenas letras")
        String senha,

        @Schema(description = "Nome da cozinha", example = "Italiana")
        @NotBlank(message = "O campo não pode ser nulo")
        @Size(min = 2, max = 800, message = "O tamanho deve ser entre 2 e 800 caracteres")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "O campo NOME deve conter apenas letras")
        String confirmacaoSenha,

        @Schema(description = "Nome da cozinha", example = "Italiana")
        @NotBlank(message = "O campo não pode ser nulo")
        @Size(min = 2, max = 800, message = "O tamanho deve ser entre 2 e 800 caracteres")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "O campo NOME deve conter apenas letras")
        String dataCadastro,

        @Schema(description = "Nome da cozinha", example = "Italiana")
        @NotBlank(message = "O campo não pode ser nulo")
        @Size(min = 2, max = 800, message = "O tamanho deve ser entre 2 e 800 caracteres")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "O campo NOME deve conter apenas letras")
        String dataAtualizacao,

        @Schema(description = "Nome da cozinha", example = "Italiana")
        @NotBlank(message = "O campo não pode ser nulo")
        @Size(min = 2, max = 800, message = "O tamanho deve ser entre 2 e 800 caracteres")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ ]+$", message = "O campo NOME deve conter apenas letras")
        Role role
) { }
