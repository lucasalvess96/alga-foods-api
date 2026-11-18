package org.estudos.algafoods.restaurante;

import java.time.LocalDateTime;

import static org.estudos.algafoods.cozinha.CozinhaConverter.toCozinhaDto;
import static org.estudos.algafoods.endereco.EnderecoConverter.toEnderecoDto;
import static org.estudos.algafoods.endereco.EnderecoConverter.toEnderecoEntity;
import static org.estudos.algafoods.pagamento.PagamentoConverter.toPagamentoDto;
import static org.estudos.algafoods.pagamento.PagamentoConverter.toPagamentoEntity;
import static org.estudos.algafoods.produto.ProdutoConverter.toProdutoDto;
import static org.estudos.algafoods.produto.ProdutoConverter.toProdutoEntity;
import static org.estudos.algafoods.usuario.UsuarioConverter.toUsuarioDtoList;

public class RestauranteConverter {

    private RestauranteConverter() {
    }

    public static RestauranteDto toRestauranteDto(Restaurante restaurante) {
        return new RestauranteDto(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getTaxaFrete(),
                restaurante.getAtivo(),
                restaurante.getAberto(),
                restaurante.getDataCadastro(),
                restaurante.getDataAtualizacao(),
                toEnderecoDto(restaurante.getEndereco()),
                toCozinhaDto(restaurante.getCozinha()),
                toProdutoDto(restaurante.getProdutos()),
                toPagamentoDto(restaurante.getFormasPagamento()),
                toUsuarioDtoList(restaurante.getResponsaveis())
        );
    }

    public static Restaurante toRestauranteEntity(RestauranteDto dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.id());
        restaurante.setNome(dto.nome());
        restaurante.setTaxaFrete(dto.taxaFrete());
        restaurante.setAtivo(dto.ativo());
        restaurante.setAberto(dto.aberto());
        restaurante.setDataCadastro(LocalDateTime.now());
        restaurante.setDataAtualizacao(LocalDateTime.now());
        restaurante.setEndereco(toEnderecoEntity(dto.endereco()));
        var produtos = toProdutoEntity(dto.produtos());
        produtos.forEach(produto -> produto.setRestaurante(restaurante));
        restaurante.setProdutos(produtos);
        restaurante.setFormasPagamento(toPagamentoEntity(dto.formasPagamento()));
        return restaurante;
    }
}
