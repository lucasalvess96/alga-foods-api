package org.estudos.algafoods.restaurante;

import org.estudos.algafoods.endereco.EnderecoDto;
import org.estudos.algafoods.produto.ProdutoDto;

import jakarta.persistence.EntityManager;
import org.estudos.algafoods.cozinha.CozinhaDto;
import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.pagamento.Pagamento;
import org.estudos.algafoods.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestauranteIntegracaoTest {
    @Test
    void cadastraProdutosEnderecoEVinculosExistentes() {
        var repository = mock(RestauranteRepository.class);
        var em = mock(EntityManager.class);
        var pagamento = new Pagamento();
        pagamento.setId(1L);
        var usuario = new Usuario();
        usuario.setId(2L);
        when(em.find(Pagamento.class, 1L)).thenReturn(pagamento);
        when(em.find(Usuario.class, 2L)).thenReturn(usuario);
        when(repository.save(any(Restaurante.class))).thenAnswer(invocation -> {
            Restaurante restaurante = invocation.getArgument(0);
            assertSame(restaurante, restaurante.getProdutos().get(0).getRestaurante());
            assertSame(pagamento, restaurante.getFormasPagamento().get(0));
            assertSame(usuario, restaurante.getResponsaveis().get(0));
            return restaurante;
        });

        var resultado = new RestauranteService(repository, em).create(dto(List.of(1L, 1L)));

        assertEquals("COMIDA BOA", resultado.nome());
        assertEquals("Rua A", resultado.endereco().logradouro());
        assertEquals("Arroz", resultado.produtos().get(0).nome());
        assertEquals(List.of(1L), resultado.formasPagamento());
        assertEquals(List.of(2L), resultado.responsaveis());
        verify(em, times(1)).find(Pagamento.class, 1L);
    }

    @Test
    void rejeitaPagamentoInexistenteSemSalvarRestaurante() {
        var repository = mock(RestauranteRepository.class);
        var em = mock(EntityManager.class);
        assertThrows(ResourceNotFoundException.class,
                () -> new RestauranteService(repository, em).create(dto(List.of(99L))));
        verify(repository, never()).save(any());
    }

    private RestauranteDto dto(List<Long> pagamentos) {
        return new RestauranteDto(null, "comida boa", BigDecimal.TEN, true, null, null,
                new CozinhaDto(null, "Brasileira"),
                new EnderecoDto("12345678", "Rua A", "10", null, "Centro"),
                List.of(new ProdutoDto("Arroz", "Porcao", BigDecimal.TEN, true)),
                pagamentos, List.of(2L));
    }
}
