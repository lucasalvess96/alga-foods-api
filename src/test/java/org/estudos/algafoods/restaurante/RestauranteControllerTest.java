package org.estudos.algafoods.restaurante;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.estudos.algafoods.cozinha.CozinhaDto;
import org.estudos.algafoods.endereco.EnderecoDto;
import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestauranteController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestauranteControllerTest {

    @MockBean
    private RestauranteService restauranteService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RestauranteDto restauranteDto;
    private List<RestauranteDto> restaurantes;

    @BeforeEach
    void setUp() {
        restauranteDto = new RestauranteDto(1L, "RESTAURANTE TESTE", new BigDecimal("10.50"),
                true, false, null, null,
                new EnderecoDto("66000000", "Avenida Presidente Vargas", "100", null, "Campina", "Belem", "PA"),
                new CozinhaDto(2L, "Brasileira"), List.of(), List.of(), List.of());
        restaurantes = List.of(restauranteDto);
    }

    @Test
    @DisplayName("Deve criar restaurante com sucesso")
    void create() throws Exception {
        when(restauranteService.create(restauranteDto)).thenReturn(restauranteDto);

        mockMvc.perform(post("/api/restaurantes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restauranteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("RESTAURANTE TESTE"))
                .andExpect(jsonPath("$.taxaFrete").value(10.50))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.aberto").value(false))
                .andExpect(jsonPath("$.endereco.cidade").value("Belem"))
                .andExpect(jsonPath("$.cozinha.id").value(2));
        verify(restauranteService).create(restauranteDto);
    }

    @Test
    @DisplayName("Deve rejeitar cadastro com campos obrigatorios invalidos")
    void createInvalido() throws Exception {
        mockMvc.perform(post("/api/restaurantes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"taxaFrete\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/restaurantes/create"));
        verifyNoInteractions(restauranteService);
    }

    @Test
    @DisplayName("Deve listar restaurantes")
    void listar() throws Exception {
        when(restauranteService.listar()).thenReturn(restaurantes);

        mockMvc.perform(get("/api/restaurantes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("RESTAURANTE TESTE"));
        verify(restauranteService).listar();
    }

    @Test
    @DisplayName("Deve retornar restaurantes paginados e ordenados")
    void pagination() throws Exception {
        PageRequest pageable = PageRequest.of(1, 5, Sort.by("nome"));
        when(restauranteService.pagination(pageable)).thenReturn(new PageImpl<>(restaurantes, pageable, 6));

        mockMvc.perform(get("/api/restaurantes/pagination")
                        .param("page", "1").param("size", "5").param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(6));
        verify(restauranteService).pagination(pageable);
    }

    @Test
    @DisplayName("Deve retornar detalhes do restaurante")
    void detail() throws Exception {
        when(restauranteService.detail(1L)).thenReturn(restauranteDto);

        mockMvc.perform(get("/api/restaurantes/detail/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("RESTAURANTE TESTE"));
        verify(restauranteService).detail(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 para restaurante inexistente")
    void detailInexistente() throws Exception {
        when(restauranteService.detail(99L)).thenThrow(new ResourceNotFoundException("Restaurante nao encontrado: 99"));

        mockMvc.perform(get("/api/restaurantes/detail/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Restaurante nao encontrado: 99"));
        verify(restauranteService).detail(99L);
    }

    @Test
    @DisplayName("Deve pesquisar restaurantes pelo nome")
    void searchList() throws Exception {
        when(restauranteService.search("TESTE")).thenReturn(restaurantes);

        mockMvc.perform(get("/api/restaurantes/search").param("nome", "TESTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("RESTAURANTE TESTE"));
        verify(restauranteService).search("TESTE");
    }

    @Test
    @DisplayName("Deve pesquisar restaurantes com paginacao")
    void searchPagination() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        when(restauranteService.searchPagination("TESTE", pageable)).thenReturn(new PageImpl<>(restaurantes, pageable, 1));

        mockMvc.perform(get("/api/restaurantes/search/pagination")
                        .param("nome", "TESTE").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("RESTAURANTE TESTE"))
                .andExpect(jsonPath("$.totalElements").value(1));
        verify(restauranteService).searchPagination("TESTE", pageable);
    }

    @Test
    @DisplayName("Deve buscar restaurantes por nome e ativo com ordenacao padrao")
    void buscar() throws Exception {
        RestauranteFiltro filtro = new RestauranteFiltro("TESTE", false);
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dataCadastro"));
        when(restauranteService.restaurantes(filtro, pageable)).thenReturn(new PageImpl<>(restaurantes, pageable, 1));

        mockMvc.perform(get("/api/restaurantes/buscar").param("nome", "TESTE").param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.size").value(10));
        verify(restauranteService).restaurantes(filtro, pageable);
    }

    @Test
    @DisplayName("Deve rejeitar pesquisa sem nome obrigatorio")
    void searchSemNome() throws Exception {
        mockMvc.perform(get("/api/restaurantes/search"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(restauranteService);
    }
}
