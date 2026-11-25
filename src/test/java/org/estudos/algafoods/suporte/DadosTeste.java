package org.estudos.algafoods.suporte;

import org.estudos.algafoods.endereco.Endereco;
import org.estudos.algafoods.endereco.EnderecoDto;

public final class DadosTeste {

    private DadosTeste() {
    }

    public static Endereco endereco() {
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCep("66000000");
        endereco.setLogradouro("Avenida Presidente Vargas");
        endereco.setNumero("100");
        endereco.setComplemento("Sala 10");
        endereco.setBairro("Campina");
        endereco.setCidade("Belem");
        endereco.setEstado("PA");
        return endereco;
    }

    public static EnderecoDto enderecoDto() {
        return new EnderecoDto("66000000", "Avenida Presidente Vargas", "100",
                "Sala 10", "Campina", "Belem", "PA");
    }
}
