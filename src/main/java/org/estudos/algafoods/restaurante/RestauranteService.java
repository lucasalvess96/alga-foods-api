package org.estudos.algafoods.restaurante;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.estudos.algafoods.pagamento.Pagamento;
import org.estudos.algafoods.usuario.Usuario;

import java.util.List;

import static org.estudos.algafoods.restaurante.RestauranteConverter.toRestauranteDto;
import static org.estudos.algafoods.restaurante.RestauranteConverter.toRestauranteEntity;

@Service
@Transactional(readOnly = true)
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final EntityManager entityManager;

    public RestauranteService(RestauranteRepository restauranteRepository, EntityManager entityManager) {
        this.restauranteRepository = restauranteRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public RestauranteDto create(RestauranteDto dto) {
        Restaurante restaurante = toRestauranteEntity(dto);
        if (dto.formasPagamento() != null) {
            dto.formasPagamento().stream().distinct().forEach(id ->
                    restaurante.getFormasPagamento().add(buscar(Pagamento.class, id)));
        }
        if (dto.responsaveis() != null) {
            dto.responsaveis().stream().distinct().forEach(id ->
                    restaurante.getResponsaveis().add(buscar(Usuario.class, id)));
        }
        return toRestauranteDto(restauranteRepository.save(restaurante));
    }

    private <T> T buscar(Class<T> tipo, Long id) {
        T entidade = entityManager.find(tipo, id);
        if (entidade == null) {
            throw new ResourceNotFoundException(tipo.getSimpleName() + " nao encontrado: " + id);
        }
        return entidade;
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
}
