package org.estudos.algafoods.cozinha;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.suporte.DadosTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CozinhaController.class)
class CozinhaControllerTest {

    @MockBean
    private CozinhaService cozinhaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CozinhaDto cozinhaDto;
    private List<CozinhaDto> cozinhas;

    @BeforeEach
    void setUp() {
        cozinhaDto = DadosTeste.cozinhaDto();
        cozinhas = DadosTeste.cozinhas();
    }

    @Test
    @DisplayName("Deve criar cozinha e retornar corpo e Location")
    void create() throws Exception {
        var entrada = new CozinhaDto(null, cozinhaDto.nome());

        when(cozinhaService.create(entrada)).thenReturn(cozinhaDto);

        var resultado = mockMvc.perform(post("/api/cozinhas/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)));

        resultado.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/cozinhas/detail/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value(cozinhaDto.nome()));

        verify(cozinhaService).create(entrada);
    }

    @Test
    @DisplayName("Deve rejeitar cadastro com nome invalido")
    void createInvalido() throws Exception {
        var entrada = new CozinhaDto(null, "123");
        var resultado = mockMvc.perform(post("/api/cozinhas/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)));

        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/cozinhas/create"));

        verifyNoInteractions(cozinhaService);
    }

    @Test
    @DisplayName("Deve listar cozinhas")
    void list() throws Exception {
        when(cozinhaService.listar()).thenReturn(cozinhas);

        var resultado = mockMvc.perform(get("/api/cozinhas/list"));

        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(cozinhas.size())))
                .andExpect(content().json(objectMapper.writeValueAsString(cozinhas)));

        verify(cozinhaService).listar();
    }

    @Test
    @DisplayName("Deve retornar cozinhas paginadas e ordenadas")
    void pagination() throws Exception {
        var pageable = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "nome"));

        when(cozinhaService.pagination(pageable)).thenReturn(new PageImpl<>(cozinhas, pageable, 4));

        var resultado = mockMvc.perform(get("/api/cozinhas/pagination")
                .param("page", "1").param("size", "2").param("sort", "nome,desc"));

        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(cozinhaDto.id()))
                .andExpect(jsonPath("$.content[0].nome").value(cozinhaDto.nome()))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(4));

        verify(cozinhaService).pagination(pageable);
    }

    @Test
    @DisplayName("Deve retornar detalhes da cozinha")
    void detail() throws Exception {
        when(cozinhaService.detail(1L)).thenReturn(cozinhaDto);

        var resultado = mockMvc.perform(get("/api/cozinhas/detail/{id}", 1L));

        resultado.andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(cozinhaDto)));

        verify(cozinhaService).detail(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao consultar cozinha inexistente")
    void detailInexistente() throws Exception {
        when(cozinhaService.detail(99L)).thenThrow(new ResourceNotFoundException("Cozinha nao encontrada"));

        var resultado = mockMvc.perform(get("/api/cozinhas/detail/{id}", 99L));

        resultado.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cozinha nao encontrada"))
                .andExpect(jsonPath("$.path").value("/api/cozinhas/detail/99"));

        verify(cozinhaService).detail(99L);
    }

    @Test
    @DisplayName("Deve atualizar cozinha pelo identificador da rota")
    void update() throws Exception {
        var entrada = new CozinhaDto(null, "Italiana");
        var atualizada = new CozinhaDto(1L, entrada.nome());

        when(cozinhaService.update(1L, entrada)).thenReturn(atualizada);

        var resultado = mockMvc.perform(put("/api/cozinhas/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)));

        resultado.andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(atualizada)));

        verify(cozinhaService).update(1L, entrada);
    }

    @Test
    @DisplayName("Deve rejeitar atualizacao com nome vazio")
    void updateInvalido() throws Exception {
        var entrada = new CozinhaDto(1L, "");
        var resultado = mockMvc.perform(put("/api/cozinhas/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)));

        resultado.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(cozinhaService);
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar cozinha inexistente")
    void updateInexistente() throws Exception {
        when(cozinhaService.update(99L, cozinhaDto)).thenThrow(new ResourceNotFoundException("Cozinha nao encontrada"));

        var resultado = mockMvc.perform(put("/api/cozinhas/update/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cozinhaDto)));

        resultado.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Cozinha nao encontrada"));

        verify(cozinhaService).update(99L, cozinhaDto);
    }

    @Test
    @DisplayName("Deve pesquisar cozinhas pelo nome")
    void searchList() throws Exception {
        when(cozinhaService.search("Brasileira")).thenReturn(List.of(cozinhaDto));

        var resultado = mockMvc.perform(get("/api/cozinhas/search").param("nome", "Brasileira"));

        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(cozinhaDto.id()))
                .andExpect(jsonPath("$[0].nome").value(cozinhaDto.nome()));

        verify(cozinhaService).search("Brasileira");
    }

    @Test
    @DisplayName("Deve rejeitar pesquisa sem parametro nome")
    void searchSemNome() throws Exception {
        String rota = "/api/cozinhas/search";
        var resultado = mockMvc.perform(get(rota));

        resultado.andExpect(status().isBadRequest());
        verifyNoInteractions(cozinhaService);
    }

    @Test
    @DisplayName("Deve excluir cozinha e retornar corpo vazio")
    void deveExcluirCozinha() throws Exception {
        Long id = cozinhaDto.id();
        var resultado = mockMvc.perform(delete("/api/cozinhas/delete/{id}", id));

        resultado.andExpect(status().isNoContent()).andExpect(content().string(""));
        verify(cozinhaService).delete(id);
    }

    @Test
    @DisplayName("Deve retornar 404 ao excluir cozinha inexistente")
    void deleteInexistente() throws Exception {
        doThrow(new ResourceNotFoundException("Cozinha nao encontrada")).when(cozinhaService).delete(99L);
        var resultado = mockMvc.perform(delete("/api/cozinhas/delete/{id}", 99L));

        resultado.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Cozinha nao encontrada"));
        verify(cozinhaService).delete(99L);
    }
}
