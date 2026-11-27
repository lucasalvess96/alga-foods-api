package org.estudos.algafoods.fotoproduto;

import org.estudos.algafoods.suporte.DadosTeste;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FotoProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
class FotoProdutoControllerTest {
    @Autowired MockMvc mvc;
    @MockBean FotoProdutoService service;
    MockMultipartFile arquivo;
    FotoProdutoDto foto;
    private static final String ROTA = "/api/restaurantes/1/salvar/produtos/2/foto";
    private static final String ROTA_ATUALIZAR = "/api/restaurantes/1/atualizar/produtos/2/foto";

    @BeforeEach
    void setUp() {
        arquivo = DadosTeste.imagemProduto();
        foto = FotoProdutoConverter.toFotoProdutoDto(DadosTeste.fotoProduto());
    }

    @Test
    @DisplayName("POST deve adicionar foto e retornar 201 e Location")
    void adicionar() throws Exception {
        when(service.adicionar(1L, 2L, arquivo, "Foto")).thenReturn(foto);

        var resultado = mvc.perform(multipart(ROTA).file(arquivo).param("descricao", "Foto"));

        resultado.andExpect(status().isCreated()).andExpect(header().string("Location", "http://localhost" + ROTA))
                .andExpect(jsonPath("$.produtoId").value(2));
        verify(service).adicionar(1L, 2L, arquivo, "Foto");
    }

    @Test
    @DisplayName("PUT deve atualizar a foto e retornar 200")
    void atualizar() throws Exception {
        when(service.atualizar(1L, 2L, arquivo, null)).thenReturn(foto);

        var resultado = mvc.perform(multipart(ROTA_ATUALIZAR).file(arquivo).with(request -> {
            request.setMethod("PUT");
            return request;
        }));

        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.produtoId").value(2));
        verify(service).atualizar(1L, 2L, arquivo, null);
    }

    @Test
    @DisplayName("Deve rejeitar requisicao sem arquivo")
    void semArquivo() throws Exception {
        var requisicao = multipart(ROTA);

        var resultado = mvc.perform(requisicao);

        resultado.andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}
