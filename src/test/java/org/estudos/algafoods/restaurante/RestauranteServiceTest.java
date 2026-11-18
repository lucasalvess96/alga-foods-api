package org.estudos.algafoods.restaurante;

import org.estudos.algafoods.cozinha.Cozinha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {

    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private Restaurante restaurante;
    @Mock
    private Cozinha cozinha;
    @Mock
    private RestauranteFiltro filtro;
    @Mock
    private PageRequest pageRequest;

    @InjectMocks
    private RestauranteService restauranteService;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("RESTAURANTE TESTE");
        restaurante.setCozinha(cozinha);

        cozinha = new Cozinha();
        cozinha.setNome("Brasileira");

        filtro = new RestauranteFiltro("RESTAURANTE TESTE", true);

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve retornar restaurantes paginados por nome")
    void restaurantes() {
        PageImpl<Restaurante> pagina = new PageImpl<>(List.of(restaurante), pageRequest, 1);

        when(restauranteRepository.buscarRestaurantes(filtro.nome(), filtro.ativo(), pageRequest)).thenReturn(pagina);

        Page<RestauranteDto> resultado = restauranteService.restaurantes(filtro, pageRequest);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().getFirst().id());
        assertEquals("RESTAURANTE TESTE", resultado.getContent().getFirst().nome());
        verify(restauranteRepository).buscarRestaurantes(filtro.nome(), filtro.ativo(), pageRequest);
    }
}
