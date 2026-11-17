package org.estudos.algafoods.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.estudos.algafoods.excecao.ErroMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Usuários", description = "Cadastro de usuários e gestão dos responsáveis por restaurantes")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Autenticação necessária", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acesso negado ou token CSRF ausente ou inválido", content = @Content)
})
public interface UsuarioSwagger {

    @Operation(summary = "Cadastrar usuário", description = "Cadastra nome e e-mail de um usuário.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado"),
                    @ApiResponse(responseCode = "400", description = "Dados de cadastro inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    ResponseEntity<UsuarioDto> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nome e e-mail do usuário", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
            UsuarioDto dto);

    @Operation(summary = "Consultar usuário pelo ID", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
    })
    UsuarioDto consultar(@Parameter(description = "ID do usuário", example = "2", required = true) Long usuarioId);

    @Operation(summary = "Listar responsáveis do restaurante", responses = {
            @ApiResponse(responseCode = "200", description = "Responsáveis cadastrados; lista vazia quando não houver vínculos"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
    })
    List<UsuarioDto> listarResponsaveis(
            @Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId);

    @Operation(summary = "Associar responsável ao restaurante",
            description = "Associa um usuário existente. Repetir a operação não duplica o vínculo. Não recebe body.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo associado ou já existente", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    ResponseEntity<Void> associarResponsavel(
            @Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId,
            @Parameter(description = "ID do usuário responsável", example = "2", required = true) Long usuarioId);

    @Operation(summary = "Remover responsável do restaurante",
            description = "Remove apenas o vínculo, preservando o usuário. Se ambos existem e não há vínculo, retorna 204. Não recebe body.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido ou já ausente", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    ResponseEntity<Void> removerResponsavel(
            @Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId,
            @Parameter(description = "ID do usuário responsável", example = "2", required = true) Long usuarioId);
}
