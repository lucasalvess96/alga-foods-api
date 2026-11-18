package org.estudos.algafoods.login;

public enum Role {
    ROLE_ADMIN,
    ROLE_GERENTE,
    ROLE_FUNCIONARIO,
    ROLE_CLIENTE,
    ROLE_ENTREGADOR;

    private final String descricao;

    private Role() {
        this.descricao = name().toLowerCase();
    }

    public String getDescricao() {
        return descricao;
    }
}
