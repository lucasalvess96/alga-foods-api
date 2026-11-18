package org.estudos.algafoods.pagamento;

import java.util.List;

public class PagamentoConverter {

    public PagamentoConverter() {
    }

    public static List<PagamentoDto> toPagamentoDto(List<Pagamento> pagamentos) {
        return pagamentos.stream().map(pagamento -> new PagamentoDto(
                pagamento.getId(),
                pagamento.getDescricao()
        )).toList();
    }

    public static List<Pagamento> toPagamentoEntity(List<PagamentoDto> dtos) {
        return dtos.stream().map(dto -> {
            Pagamento pagamento = new Pagamento();
            pagamento.setId(dto.id());
            pagamento.setDescricao(dto.descricao());
            return pagamento;
        }).toList();
    }
}
