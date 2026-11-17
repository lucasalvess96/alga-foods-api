package org.estudos.algafoods.usuario;

public final class UsuarioConverter {

    private UsuarioConverter() {
    }

    public static UsuarioDto toDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
