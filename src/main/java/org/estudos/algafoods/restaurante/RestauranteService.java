package org.estudos.algafoods.restaurante;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.usuario.UsuarioRepository;
import org.estudos.algafoods.cozinha.CozinhaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.estudos.algafoods.restaurante.RestauranteConverter.toRestauranteEntity;

@Service
@Transactional(readOnly = true)
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CozinhaRepository cozinhaRepository;

    public RestauranteService(RestauranteRepository restauranteRepository, UsuarioRepository usuarioRepository,
                              CozinhaRepository cozinhaRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.cozinhaRepository = cozinhaRepository;
    }

    @Transactional
    public RestauranteDto create(RestauranteDto dto) {
        if (dto.cozinha() == null || dto.cozinha().id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID da cozinha deve ser informado");
        }
        var cozinha = cozinhaRepository.findById(dto.cozinha().id())
                .orElseThrow(() -> new ResourceNotFoundException("Cozinha nao encontrada: " + dto.cozinha().id()));
        Restaurante restaurante = toRestauranteEntity(dto);
        restaurante.setCozinha(cozinha);
        if (dto.responsaveis() != null) {
            var responsaveis = dto.responsaveis().stream()
                    .map(responsavel -> {
                        if (responsavel.id() == null) {
                            throw new IllegalArgumentException("O ID do responsavel deve ser informado");
                        }
                        return usuarioRepository.findById(responsavel.id())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Usuario nao encontrado: " + responsavel.id()));
                    })
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList::new));
            restaurante.setResponsaveis(responsaveis);
        }
        return RestauranteConverter.toRestauranteDto(restauranteRepository.save(restaurante));
    }

    public List<RestauranteDto> listar() {
        return restauranteRepository.findAll().stream()
                .map(RestauranteConverter::toRestauranteDto)
                .toList();
    }

    public Page<RestauranteDto> pagination(Pageable pageable) {
        return restauranteRepository.findAll(pageable)
                .map(RestauranteConverter::toRestauranteDto);
    }

    public Restaurante buscarRestaurante(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante nao encontrado: " + id));
    }

    public RestauranteDto detail(Long cozinhaId) {
        return restauranteRepository.findById(cozinhaId)
                .map(RestauranteConverter::toRestauranteDto)
                .orElseThrow(() -> new ResourceNotFoundException("Informações não encontradas para: " + cozinhaId));
    }

    public List<RestauranteDto> search(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(RestauranteConverter::toRestauranteDto).toList();
    }

    public Page<RestauranteDto> searchPagination(String nome, Pageable pageable) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome, pageable).map(RestauranteConverter::toRestauranteDto);
    }

    public Page<RestauranteDto> restaurantes(RestauranteFiltro filtro, Pageable pageable) {
        return restauranteRepository.buscarRestaurantes(filtro.nome(), filtro.ativo(), pageable)
                .map(RestauranteConverter::toRestauranteDto);
    }
}
