package org.estudos.algafoods.usuario;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.restaurante.Restaurante;
import org.estudos.algafoods.restaurante.RestauranteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RestauranteService restauranteService;

    public UsuarioService(UsuarioRepository usuarioRepository, RestauranteService restauranteService) {
        this.usuarioRepository = usuarioRepository;
        this.restauranteService = restauranteService;
    }

    @Transactional
    public UsuarioDto criar(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setDataCadastro(LocalDate.now());
        usuario.setDataAtualizacao(usuario.getDataCadastro());
        return UsuarioConverter.toDto(usuarioRepository.save(usuario));
    }

    public UsuarioDto consultar(Long usuarioId) {
        return UsuarioConverter.toDto(buscarUsuario(usuarioId));
    }

    public List<UsuarioDto> listarResponsaveis(Long restauranteId) {
        return restauranteService.buscarRestaurante(restauranteId).getResponsaveis().stream()
                .map(UsuarioConverter::toDto).toList();
    }

    @Transactional
    public void associarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = restauranteService.buscarRestaurante(restauranteId);
        Usuario usuario = buscarUsuario(usuarioId);
        if (restaurante.getResponsaveis().stream().noneMatch(u -> u.getId().equals(usuarioId))) {
            restaurante.getResponsaveis().add(usuario);
        }
    }

    @Transactional
    public void removerResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = restauranteService.buscarRestaurante(restauranteId);
        buscarUsuario(usuarioId);
        restaurante.getResponsaveis().removeIf(u -> u.getId().equals(usuarioId));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado: " + id));
    }
}
