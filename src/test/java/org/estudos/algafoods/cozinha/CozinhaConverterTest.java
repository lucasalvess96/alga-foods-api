package org.estudos.algafoods.cozinha;

import org.estudos.algafoods.suporte.DadosTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CozinhaConverterTest {

    private Cozinha cozinha;
    private CozinhaDto dto;

    @BeforeEach
    void setUp() {
        cozinha = DadosTeste.cozinha();
        dto = DadosTeste.cozinhaDto();
    }

    @Test
    @DisplayName("Deve converter identificador e nome da entidade para DTO")
    void deveConverterParaDto() {
        var esperado = new CozinhaDto(dto.id(), "BRASILEIRA");

        var resultado = CozinhaConverter.toCozinhaDto(cozinha);

        assertEquals(esperado, resultado);
    }

    @Test
    @DisplayName("Deve converter DTO para entidade com nome em maiusculas")
    void deveConverterParaEntidade() {
        var entrada = dto;

        var resultado = CozinhaConverter.toCozinhaEntity(entrada);

        assertEquals(entrada.id(), resultado.getId());
        assertEquals("BRASILEIRA", resultado.getNome());
        assertEquals(0, resultado.getRestaurantes().size());
    }

    @Test
    @DisplayName("Deve remover espacos excedentes do nome ao converter para entidade")
    void deveNormalizarEspacos() {
        var entrada = new CozinhaDto(dto.id(), "  Comida   Brasileira  ");

        var resultado = CozinhaConverter.toCozinhaEntity(entrada);

        assertEquals(entrada.id(), resultado.getId());
        assertEquals("COMIDA BRASILEIRA", resultado.getNome());
    }

    @Test
    @DisplayName("Deve preservar identificador nulo ao converter DTO de criacao")
    void deveConverterSemId() {
        var entrada = new CozinhaDto(null, dto.nome());

        var resultado = CozinhaConverter.toCozinhaEntity(entrada);

        assertEquals("BRASILEIRA", resultado.getNome());
    }
}
