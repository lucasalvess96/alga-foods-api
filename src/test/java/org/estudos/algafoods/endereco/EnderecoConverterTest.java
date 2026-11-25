package org.estudos.algafoods.endereco;

import org.estudos.algafoods.suporte.DadosTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EnderecoConverterTest {

    private Endereco endereco;
    private EnderecoDto dto;

    @BeforeEach
    void setUp() {
        endereco = DadosTeste.endereco();
        dto = DadosTeste.enderecoDto();
    }

    @Test
    @DisplayName("Deve converter todos os campos do endereco para DTO")
    void deveConverterEnderecoParaDto() {
        EnderecoDto esperado = dto;

        EnderecoDto resultado = EnderecoConverter.toEnderecoDto(endereco);

        assertEquals(esperado, resultado);
    }

    @Test
    @DisplayName("Deve converter todos os campos do DTO para entidade sem identificador")
    void deveConverterDtoParaEntidade() {
        EnderecoDto entrada = dto;

        Endereco resultado = EnderecoConverter.toEnderecoEntity(entrada);

        assertEquals(entrada.cep(), resultado.getCep());
        assertEquals(entrada.logradouro(), resultado.getLogradouro());
        assertEquals(entrada.numero(), resultado.getNumero());
        assertEquals(entrada.complemento(), resultado.getComplemento());
        assertEquals(entrada.bairro(), resultado.getBairro());
        assertEquals(entrada.cidade(), resultado.getCidade());
        assertEquals(entrada.estado(), resultado.getEstado());
    }

    @Test
    @DisplayName("Deve preservar complemento ausente ao converter endereco para DTO")
    void deveConverterEnderecoSemComplementoParaDto() {
        endereco.setComplemento(null);
        EnderecoDto esperado = new EnderecoDto(dto.cep(), dto.logradouro(), dto.numero(),
                null, dto.bairro(), dto.cidade(), dto.estado());

        EnderecoDto resultado = EnderecoConverter.toEnderecoDto(endereco);

        assertEquals(esperado, resultado);
    }

    @Test
    @DisplayName("Deve preservar complemento ausente ao converter DTO para entidade")
    void deveConverterDtoSemComplementoParaEntidade() {
        EnderecoDto entrada = new EnderecoDto(dto.cep(), dto.logradouro(), dto.numero(),
                null, dto.bairro(), dto.cidade(), dto.estado());

        Endereco resultado = EnderecoConverter.toEnderecoEntity(entrada);

        assertEquals(entrada.cep(), resultado.getCep());
        assertEquals(entrada.logradouro(), resultado.getLogradouro());
        assertEquals(entrada.numero(), resultado.getNumero());
        assertEquals(entrada.bairro(), resultado.getBairro());
        assertEquals(entrada.cidade(), resultado.getCidade());
        assertEquals(entrada.estado(), resultado.getEstado());
    }
}
