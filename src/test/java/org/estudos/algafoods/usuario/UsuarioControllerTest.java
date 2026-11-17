package org.estudos.algafoods.usuario;

import org.estudos.algafoods.excecao.HandleErro;
import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest {
    private final UsuarioService service = mock(UsuarioService.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new UsuarioController(service))
            .setControllerAdvice(new HandleErro()).build();

    @Test
    void criaUsuarioEConsulta() throws Exception {
        var usuario = new UsuarioDto(2L, "Maria", "maria@example.com");
        when(service.criar(any())).thenReturn(usuario);
        when(service.consultar(2L)).thenReturn(usuario);
        mvc.perform(post("/api/usuarios/criar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria\",\"email\":\"maria@example.com\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(2));
        mvc.perform(get("/api/usuarios/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void rejeitaCadastroInvalido() throws Exception {
        mvc.perform(post("/api/usuarios/criar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"email\":\"invalido\"}"))
                .andExpect(status().isBadRequest());
        verify(service, never()).criar(any());
    }

    @Test
    void listaAssociaERemoveResponsavel() throws Exception {
        when(service.listarResponsaveis(1L)).thenReturn(List.of(new UsuarioDto(2L, "Maria", "maria@example.com")));
        mvc.perform(get("/api/restaurantes/1/responsaveis")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
        mvc.perform(put("/api/restaurantes/1/responsaveis/2")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/restaurantes/1/responsaveis/2")).andExpect(status().isNoContent());
        verify(service).associarResponsavel(1L, 2L);
        verify(service).removerResponsavel(1L, 2L);
    }

    @Test
    void retorna404ParaRestauranteInexistente() throws Exception {
        when(service.listarResponsaveis(99L)).thenThrow(new ResourceNotFoundException("Restaurante nao encontrado"));
        mvc.perform(get("/api/restaurantes/99/responsaveis")).andExpect(status().isNotFound());
    }
}
