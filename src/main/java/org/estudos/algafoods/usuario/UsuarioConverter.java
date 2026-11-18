package org.estudos.algafoods.usuario;

import java.util.List;

public final class UsuarioConverter {

    private UsuarioConverter() {
    }

    public static UsuarioDto toUsuarioDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public static List<Usuario> toUsuarioEntity(List<UsuarioDto> dtos) {
        return dtos.stream().map(dto -> {
            Usuario usuario = new Usuario();
            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            return usuario;
        }).toList();
    }

    public static List<UsuarioDto> toUsuarioDtoList(List<Usuario> usuarios) {
        return usuarios.stream().map(usuario -> new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        )).toList();
    }
}
