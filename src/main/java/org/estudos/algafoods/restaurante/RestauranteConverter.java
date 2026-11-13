package org.estudos.algafoods.restaurante;

import org.estudos.algafoods.endereco.EnderecoDto;
import org.estudos.algafoods.produto.ProdutoDto;

import java.time.LocalDateTime;
import org.estudos.algafoods.endereco.Endereco;
import org.estudos.algafoods.produto.Produto;

import static org.estudos.algafoods.cozinha.CozinhaConverter.toCozinhaDto;
import static org.estudos.algafoods.cozinha.CozinhaConverter.toCozinhaEntity;

public class RestauranteConverter {

    private RestauranteConverter() {
    }

    public static RestauranteDto toRestauranteDto(Restaurante restaurante) {
        return new RestauranteDto(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getTaxaFrete(),
                restaurante.getAtivo(),
                restaurante.getDataCadastro(),
                restaurante.getDataAtualizacao(),
                toCozinhaDto(restaurante.getCozinha()),
                toEnderecoDto(restaurante.getEndereco()),
                restaurante.getProdutos().stream().map(p -> new ProdutoDto(
                        p.getNome(), p.getDescricao(), p.getPreco(), p.getAtivo())).toList(),
                restaurante.getFormasPagamento().stream().map(p -> p.getId()).toList(),
                restaurante.getResponsaveis().stream().map(u -> u.getId()).toList()
        );
    }

    public static Restaurante toRestauranteEntity(RestauranteDto dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.id());
        restaurante.setNome(dto.nome());
        restaurante.setTaxaFrete(dto.taxaFrete());
        restaurante.setAtivo(dto.ativo());
        restaurante.setDataCadastro(LocalDateTime.now());
        restaurante.setDataAtualizacao(LocalDateTime.now());
        restaurante.setCozinha(toCozinhaEntity(dto.cozinha()));
        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setCep(dto.endereco().cep());
            endereco.setLogradouro(dto.endereco().logradouro());
            endereco.setNumero(dto.endereco().numero());
            endereco.setComplemento(dto.endereco().complemento());
            endereco.setBairro(dto.endereco().bairro());
            restaurante.setEndereco(endereco);
        }
        if (dto.produtos() != null) {
            for (ProdutoDto item : dto.produtos()) {
                Produto produto = new Produto();
                produto.setNome(item.nome());
                produto.setDescricao(item.descricao());
                produto.setPreco(item.preco());
                produto.setAtivo(item.ativo());
                restaurante.adicionarProduto(produto);
            }
        }
        return restaurante;
    }

    private static EnderecoDto toEnderecoDto(Endereco endereco) {
        return endereco == null ? null : new EnderecoDto(
                endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(),
                endereco.getComplemento(), endereco.getBairro());
    }
}
