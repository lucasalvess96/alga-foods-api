package org.estudos.algafoods.restaurante;

import org.estudos.algafoods.cozinha.Cozinha;
import org.estudos.algafoods.cozinha.CozinhaDto;
import org.estudos.algafoods.endereco.Endereco;
import org.estudos.algafoods.endereco.EnderecoDto;
import org.estudos.algafoods.pagamento.Pagamento;
import org.estudos.algafoods.pagamento.PagamentoDto;
import org.estudos.algafoods.produto.Produto;
import org.estudos.algafoods.produto.ProdutoDto;
import org.estudos.algafoods.usuario.Usuario;
import org.estudos.algafoods.usuario.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.estudos.algafoods.endereco.EnderecoConverter.toEnderecoDto;

class RestauranteConverterTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        Cozinha cozinha = new Cozinha();
        cozinha.setId(2L);
        cozinha.setNome("Brasileira");

        Endereco endereco = new Endereco();
        endereco.setCep("66000000");
        endereco.setLogradouro("Avenida Presidente Vargas");
        endereco.setNumero("100");
        endereco.setComplemento("Sala 10");
        endereco.setBairro("Campina");
        endereco.setCidade("Belem");
        endereco.setEstado("PA");

        Produto produto = new Produto();
        produto.setNome("Prato feito");
        produto.setDescricao("Arroz e feijao");
        produto.setPreco(new BigDecimal("25.90"));
        produto.setAtivo(true);

        Pagamento pagamento = new Pagamento();
        pagamento.setId(3L);
        pagamento.setDescricao("Pix");

        Usuario responsavel = new Usuario();
        responsavel.setId(4L);
        responsavel.setNome("Maria");
        responsavel.setEmail("maria@example.com");

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("RESTAURANTE TESTE");
        restaurante.setTaxaFrete(new BigDecimal("10.50"));
        restaurante.setAtivo(true);
        restaurante.setAberto(false);
        restaurante.setDataCadastro(LocalDateTime.of(2025, 1, 1, 10, 0));
        restaurante.setDataAtualizacao(LocalDateTime.of(2025, 1, 2, 11, 30));
        restaurante.setCozinha(cozinha);
        restaurante.setEndereco(endereco);
        restaurante.setProdutos(List.of(produto));
        restaurante.setFormasPagamento(List.of(pagamento));
        restaurante.setResponsaveis(List.of(responsavel));
    }

    @Test
    @DisplayName("Deve converter restaurante para DTO com seus relacionamentos")
    void toRestauranteDto() {
        RestauranteDto resultado = RestauranteConverter.toRestauranteDto(restaurante);

        assertEquals(restaurante.getId(), resultado.id());
        assertEquals(restaurante.getNome(), resultado.nome());
        assertEquals(restaurante.getTaxaFrete(), resultado.taxaFrete());
        assertEquals(restaurante.getAtivo(), resultado.ativo());
        assertEquals(restaurante.getAberto(), resultado.aberto());
        assertEquals(restaurante.getDataCadastro(), resultado.dataCadastro());
        assertEquals(restaurante.getDataAtualizacao(), resultado.dataAtualizacao());
        assertEquals(new EnderecoDto("66000000",
                "Avenida Presidente Vargas", "100", "Sala 10", "Campina", "Belem", "PA"), resultado.endereco());
        assertEquals(new CozinhaDto(2L, "BRASILEIRA"), resultado.cozinha());
        assertEquals(List.of(new ProdutoDto("Prato feito",
                "Arroz e feijao", new BigDecimal("25.90"), true)), resultado.produtos());
        assertEquals(List.of(new PagamentoDto(3L, "Pix")), resultado.formasPagamento());
        assertEquals(List.of(new UsuarioDto(4L, "MARIA", "maria@example.com")), resultado.responsaveis());
    }

    @Test
    @DisplayName("Deve converter DTO para entidade e vincular os produtos ao restaurante")
    void toRestauranteEntity() {
        RestauranteDto dto = RestauranteConverter.toRestauranteDto(restaurante);
        LocalDateTime inicio = LocalDateTime.now();

        Restaurante resultado = RestauranteConverter.toRestauranteEntity(dto);

        LocalDateTime fim = LocalDateTime.now();
        assertEquals(dto.id(), resultado.getId());
        assertEquals(dto.nome(), resultado.getNome());
        assertEquals(dto.taxaFrete(), resultado.getTaxaFrete());
        assertEquals(dto.ativo(), resultado.getAtivo());
        assertEquals(dto.aberto(), resultado.getAberto());
        assertFalse(resultado.getDataCadastro().isBefore(inicio));
        assertFalse(resultado.getDataCadastro().isAfter(fim));
        assertFalse(resultado.getDataAtualizacao().isBefore(inicio));
        assertFalse(resultado.getDataAtualizacao().isAfter(fim));
        assertEquals(dto.endereco(), toEnderecoDto(resultado.getEndereco()));
        assertEquals(1, resultado.getProdutos().size());
        Produto produto = resultado.getProdutos().getFirst();
        assertEquals(dto.produtos().getFirst().nome(), produto.getNome());
        assertEquals(dto.produtos().getFirst().descricao(), produto.getDescricao());
        assertEquals(dto.produtos().getFirst().preco(), produto.getPreco());
        assertEquals(dto.produtos().getFirst().ativo(), produto.getAtivo());
        assertSame(resultado, produto.getRestaurante());
        assertEquals(1, resultado.getFormasPagamento().size());
        assertEquals(3L, resultado.getFormasPagamento().getFirst().getId());
        assertEquals("Pix", resultado.getFormasPagamento().getFirst().getDescricao());
    }

    @Test
    @DisplayName("Deve converter restaurante sem produtos, pagamentos e responsaveis")
    void converterComListasVazias() {
        restaurante.setProdutos(List.of());
        restaurante.setFormasPagamento(List.of());
        restaurante.setResponsaveis(List.of());

        RestauranteDto dto = RestauranteConverter.toRestauranteDto(restaurante);
        Restaurante resultado = RestauranteConverter.toRestauranteEntity(dto);

        assertTrue(dto.produtos().isEmpty());
        assertTrue(dto.formasPagamento().isEmpty());
        assertTrue(dto.responsaveis().isEmpty());
        assertTrue(resultado.getProdutos().isEmpty());
        assertTrue(resultado.getFormasPagamento().isEmpty());
    }
}
