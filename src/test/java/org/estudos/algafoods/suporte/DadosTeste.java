package org.estudos.algafoods.suporte;

import org.estudos.algafoods.cozinha.Cozinha;
import org.estudos.algafoods.cozinha.CozinhaDto;
import org.estudos.algafoods.endereco.Endereco;
import org.estudos.algafoods.endereco.EnderecoDto;

import java.util.ArrayList;
import java.util.List;

public final class DadosTeste {

    private DadosTeste() {
    }

    public static Cozinha cozinha() {
        Cozinha cozinha = new Cozinha();
        cozinha.setId(1L);
        cozinha.setNome("Brasileira");
        return cozinha;
    }

    public static CozinhaDto cozinhaDto() {
        return new CozinhaDto(1L, "Brasileira");
    }

    public static List<CozinhaDto> cozinhas() {
        return new ArrayList<>(List.of(cozinhaDto(), new CozinhaDto(2L, "Italiana")));
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
