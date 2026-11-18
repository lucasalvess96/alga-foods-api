package org.estudos.algafoods.produto;

import java.util.List;

public class ProdutoConverter {

    public ProdutoConverter() {
    }

    public static List<ProdutoDto> toProdutoDto(List<Produto> produtos) {
        return produtos.stream().map(produto -> new ProdutoDto(
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getAtivo()
        )).toList();
    }

    public static List<Produto> toProdutoEntity(List<ProdutoDto> dtos) {
        return dtos.stream().map(dto -> {
            Produto produto = new Produto();
            produto.setNome(dto.nome());
            produto.setDescricao(dto.descricao());
            produto.setPreco(dto.preco());
            produto.setAtivo(dto.ativo());
            return produto;
        }).toList();
    }
}
