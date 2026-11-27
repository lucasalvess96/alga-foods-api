package org.estudos.algafoods.fotoproduto;

public final class FotoProdutoConverter {

    private FotoProdutoConverter() {
    }

    public static FotoProdutoDto toFotoProdutoDto(FotoProduto foto) {
        return new FotoProdutoDto(
                foto.getId(),
                foto.getProduto().getId(),
                foto.getNome(),
                foto.getDescricao(),
                foto.getContentType(),
                foto.getTamanho()
        );
    }

    public static FotoProduto toFotoProduto(FotoProdutoDto dto) {
        var foto = new FotoProduto();
        foto.setId(dto.id());
        foto.setNome(dto.nome());
        foto.setDescricao(dto.descricao());
        foto.setContentType(dto.contentType());
        foto.setTamanho(dto.tamanho());
        return foto;
    }
}
