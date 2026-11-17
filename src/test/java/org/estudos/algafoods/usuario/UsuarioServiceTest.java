package org.estudos.algafoods.usuario;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.restaurante.Restaurante;
import org.estudos.algafoods.restaurante.RestauranteService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    private final UsuarioRepository usuarios = mock(UsuarioRepository.class);
    private final RestauranteService restaurantes = mock(RestauranteService.class);
    private final UsuarioService service = new UsuarioService(usuarios, restaurantes);

    @Test
    void criaUsuarioComDatasSemReutilizarIdDeEntrada() {
        when(usuarios.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            assertNull(usuario.getId());
            assertNotNull(usuario.getDataCadastro());
            assertEquals(usuario.getDataCadastro(), usuario.getDataAtualizacao());
            usuario.setId(2L);
            return usuario;
        });
        var resultado = service.criar(new UsuarioDto(99L, "Maria", "maria@example.com"));
        assertEquals(2L, resultado.id());
        assertEquals("MARIA", resultado.nome());
    }

    @Test
    void associaSemDuplicarListaERemoveSomenteVinculo() {
        var restaurante = new Restaurante();
        var usuario = new Usuario();
        usuario.setId(2L);
        when(restaurantes.buscarRestaurante(1L)).thenReturn(restaurante);
        when(usuarios.findById(2L)).thenReturn(Optional.of(usuario));

        service.associarResponsavel(1L, 2L);
        service.associarResponsavel(1L, 2L);
        assertEquals(1, service.listarResponsaveis(1L).size());
        assertEquals(2L, service.listarResponsaveis(1L).get(0).id());
        service.removerResponsavel(1L, 2L);
        service.removerResponsavel(1L, 2L);
        assertTrue(restaurante.getResponsaveis().isEmpty());
        verify(usuarios, never()).delete(any());
    }

    @Test
    void rejeitaUsuarioInexistenteSemAlterarResponsaveis() {
        var restaurante = new Restaurante();
        when(restaurantes.buscarRestaurante(1L)).thenReturn(restaurante);
        assertThrows(ResourceNotFoundException.class, () -> service.associarResponsavel(1L, 9L));
        assertTrue(restaurante.getResponsaveis().isEmpty());
    }

    @Test
    void rejeitaRestauranteInexistente() {
        when(restaurantes.buscarRestaurante(99L))
                .thenThrow(new ResourceNotFoundException("Restaurante nao encontrado: 99"));
        assertThrows(ResourceNotFoundException.class, () -> service.listarResponsaveis(99L));
        assertThrows(ResourceNotFoundException.class, () -> service.associarResponsavel(99L, 2L));
        assertThrows(ResourceNotFoundException.class, () -> service.removerResponsavel(99L, 2L));
    }
}
