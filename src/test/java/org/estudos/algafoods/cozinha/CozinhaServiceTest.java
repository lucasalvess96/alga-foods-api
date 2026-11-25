package org.estudos.algafoods.cozinha;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.suporte.DadosTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CozinhaServiceTest {

    @Mock
    private CozinhaRepository cozinhaRepository;

    @InjectMocks
    private CozinhaService service;

    private Cozinha cozinha;
    private CozinhaDto dto;

    @BeforeEach
    void setUp() {
        cozinha = DadosTeste.cozinha();
        dto = DadosTeste.cozinhaDto();
    }

    @Test
    @DisplayName("Deve converter entrada, salvar cozinha e retornar DTO persistido")
    void create() {
        var entrada = new CozinhaDto(null, "  Comida   Brasileira  ");
        cozinha.setNome("Comida Brasileira");
        when(cozinhaRepository.save(any(Cozinha.class))).thenReturn(cozinha);

        var resultado = service.create(entrada);

        var captor = ArgumentCaptor.forClass(Cozinha.class);
        verify(cozinhaRepository).save(captor.capture());
        assertEquals("COMIDA BRASILEIRA", captor.getValue().getNome());
        assertEquals(new CozinhaDto(dto.id(), "COMIDA BRASILEIRA"), resultado);
    }

    @Test
    @DisplayName("Deve listar cozinhas convertidas para DTO")
    void listar() {
        var segunda = DadosTeste.cozinha();
        segunda.setId(2L);
        segunda.setNome("Italiana");
        when(cozinhaRepository.findAll()).thenReturn(List.of(cozinha, segunda));

        var resultado = service.listar();

        assertEquals(List.of(new CozinhaDto(dto.id(), "BRASILEIRA"), new CozinhaDto(2L, "ITALIANA")), resultado);
        verify(cozinhaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver cozinhas")
    void listarVazio() {
        when(cozinhaRepository.findAll()).thenReturn(List.of());

        var resultado = service.listar();

        assertEquals(List.of(), resultado);
    }

    @Test
    @DisplayName("Deve converter pagina preservando paginacao e total de elementos")
    void pagination() {
        var pageable = PageRequest.of(1, 2, Sort.by("nome"));

        when(cozinhaRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(cozinha), pageable, 3));

        var resultado = service.pagination(pageable);

        assertEquals(List.of(new CozinhaDto(dto.id(), "BRASILEIRA")), resultado.getContent());
        assertEquals(pageable, resultado.getPageable());
        assertEquals(3L, resultado.getTotalElements());

        verify(cozinhaRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar detalhes da cozinha encontrada")
    void detail() {
        when(cozinhaRepository.findById(dto.id())).thenReturn(Optional.of(cozinha));

        var resultado = service.detail(dto.id());

        assertEquals(new CozinhaDto(dto.id(), "BRASILEIRA"), resultado);
        verify(cozinhaRepository).findById(dto.id());
    }

    @Test
    @DisplayName("Deve lancar excecao ao consultar cozinha inexistente")
    void detailInexistente() {
        when(cozinhaRepository.findById(99L)).thenReturn(Optional.empty());

        var erro = assertThrows(ResourceNotFoundException.class, () -> service.detail(99L));

        assertEquals("Informações não encontradas para: 99", erro.getMessage());
        verify(cozinhaRepository).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar nome normalizado usando identificador informado ao servico")
    void update() {
        var entrada = new CozinhaDto(99L, "  Comida   Italiana  ");
        cozinha.setNome("Comida Italiana");
        when(cozinhaRepository.existsById(dto.id())).thenReturn(true);
        when(cozinhaRepository.save(any(Cozinha.class))).thenReturn(cozinha);

        var resultado = service.update(dto.id(), entrada);

        var captor = ArgumentCaptor.forClass(Cozinha.class);
        verify(cozinhaRepository).save(captor.capture());
        assertEquals(dto.id(), captor.getValue().getId());
        assertEquals("COMIDA ITALIANA", captor.getValue().getNome());
        assertEquals(new CozinhaDto(dto.id(), "COMIDA ITALIANA"), resultado);
    }

    @Test
    @DisplayName("Deve impedir gravacao ao atualizar cozinha inexistente")
    void updateInexistente() {
        when(cozinhaRepository.existsById(99L)).thenReturn(false);
        when(cozinhaRepository.findById(99L)).thenReturn(Optional.empty());

        var erro = assertThrows(ResourceNotFoundException.class, () -> service.update(99L, dto));

        assertEquals("Informações não encontradas para: 99", erro.getMessage());
        verify(cozinhaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve pesquisar por nome e retornar DTOs encontrados")
    void search() {
        when(cozinhaRepository.findByNomeContainingIgnoreCase("brasi")).thenReturn(List.of(cozinha));

        var resultado = service.search("brasi");

        assertEquals(List.of(new CozinhaDto(dto.id(), "BRASILEIRA")), resultado);
        verify(cozinhaRepository).findByNomeContainingIgnoreCase("brasi");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando pesquisa nao encontrar cozinhas")
    void searchVazio() {
        when(cozinhaRepository.findByNomeContainingIgnoreCase("inexistente")).thenReturn(List.of());

        var resultado = service.search("inexistente");

        assertEquals(List.of(), resultado);
    }

    @Test
    @DisplayName("Deve excluir cozinha existente pelo identificador")
    void delete() {
        when(cozinhaRepository.findById(dto.id())).thenReturn(Optional.of(cozinha));

        service.delete(dto.id());

        verify(cozinhaRepository).findById(dto.id());
        verify(cozinhaRepository).deleteById(dto.id());
    }

    @Test
    @DisplayName("Deve impedir exclusao de cozinha inexistente")
    void deleteInexistente() {
        when(cozinhaRepository.findById(99L)).thenReturn(Optional.empty());

        var erro = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        assertEquals("Recurso não encontrado", erro.getMessage());
        verify(cozinhaRepository, never()).deleteById(anyLong());
    }
}
