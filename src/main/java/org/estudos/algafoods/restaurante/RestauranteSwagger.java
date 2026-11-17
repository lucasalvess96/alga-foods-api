package org.estudos.algafoods.restaurante;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.estudos.algafoods.excecao.ErroMessage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Restaurantes", description = "Cadastro e consulta de restaurantes")
public interface RestauranteSwagger {

    @Operation(summary = "Criar um restaurante",
            description = "Cadastra restaurante com cozinha, endereco e produtos. formasPagamento e responsaveis referenciam registros existentes; os novos campos sao opcionais.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados de cadastro inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    ResponseEntity<RestauranteDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do restaurante", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteDto.class)))
            RestauranteDto restauranteDto);

    @Operation(summary = "Listar todos os restaurantes",
            responses = @ApiResponse(responseCode = "200", description = "Lista de restaurantes; vazia quando não houver registros"))
    ResponseEntity<List<RestauranteDto>> listarCozinhas();

    @Operation(summary = "Listar restaurantes com paginação",
            responses = @ApiResponse(responseCode = "200", description = "Página de restaurantes retornada com sucesso"))
    ResponseEntity<Page<RestauranteDto>> pagination(@ParameterObject Pageable pageable);

    @Operation(summary = "Consultar um restaurante pelo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    ResponseEntity<RestauranteDto> detail(
            @Parameter(description = "ID do restaurante", example = "1", required = true) Long id);

    @Operation(summary = "Buscar restaurantes pelo nome",
            description = "Busca por parte do nome, sem diferenciar letras maiúsculas e minúsculas.",
            responses = @ApiResponse(responseCode = "200", description = "Lista de restaurantes; vazia quando não houver resultados"))
    ResponseEntity<List<RestauranteDto>> searchList(
            @Parameter(description = "Nome ou parte do nome do restaurante", example = "Italiano", required = true) String nome);

    @Operation(summary = "Buscar restaurantes pelo nome com paginação",
            description = "Busca por parte do nome, sem diferenciar letras maiúsculas e minúsculas, com resultado paginado.",
            responses = @ApiResponse(responseCode = "200", description = "Página de restaurantes retornada com sucesso"))
    ResponseEntity<Page<RestauranteDto>> searchPagination(
            @Parameter(description = "Nome ou parte do nome do restaurante", example = "Italiano", required = true) String nome,
            @ParameterObject Pageable pageable);
}
