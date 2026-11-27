package org.estudos.algafoods.fotoproduto;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.minio.MinioStorageService;
import org.estudos.algafoods.produto.ProdutoRepository;
import org.estudos.algafoods.suporte.DadosTeste;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FotoProdutoServiceTest {
    @Mock ProdutoRepository produtos;
    @Mock FotoProdutoRepository fotos;
    @Mock MinioStorageService storage;
    @InjectMocks FotoProdutoService service;
    FotoProduto foto;
    MockMultipartFile arquivo;

    @BeforeEach
    void setUp() {
        foto = DadosTeste.fotoProduto();
        arquivo = DadosTeste.imagemProduto();
        TransactionSynchronizationManager.initSynchronization();
    }

    @AfterEach
    void limpar() {
        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    @DisplayName("Deve cadastrar os metadados e manter o novo objeto apos commit")
    void adicionar() {
        when(produtos.findByIdAndRestauranteId(2L, 1L)).thenReturn(Optional.of(foto.getProduto()));
        when(storage.enviar(arquivo)).thenReturn("produtos/nova");
        when(fotos.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.adicionar(1L, 2L, arquivo, "Foto");
        concluir(TransactionSynchronization.STATUS_COMMITTED);

        assertEquals(2L, resultado.produtoId());
        assertEquals("Foto", resultado.descricao());
        var captor = ArgumentCaptor.forClass(FotoProduto.class);
        verify(fotos).saveAndFlush(captor.capture());
        assertEquals("produtos/nova", captor.getValue().getChaveObjeto());
        verify(storage, never()).excluir(any());
    }

    @Test
    @DisplayName("Deve remover a foto anterior somente depois do commit da atualizacao")
    void atualizar() {
        prepararAtualizacao();
        when(fotos.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.atualizar(1L, 2L, arquivo, null);

        verify(storage, never()).excluir(any());
        concluir(TransactionSynchronization.STATUS_COMMITTED);
        verify(storage).excluir("produtos/antiga");
        assertEquals("produtos/nova", foto.getChaveObjeto());
    }

    @Test
    @DisplayName("Deve limpar o novo objeto e preservar o antigo quando o banco falhar")
    void rollback() {
        prepararAtualizacao();
        when(fotos.saveAndFlush(any())).thenThrow(new IllegalStateException("banco"));

        assertThrows(IllegalStateException.class, () -> service.atualizar(1L, 2L, arquivo, null));
        concluir(TransactionSynchronization.STATUS_ROLLED_BACK);

        verify(storage).excluir("produtos/nova");
        verify(storage, never()).excluir("produtos/antiga");
    }

    @Test
    @DisplayName("Deve rejeitar cadastro duplicado antes do upload")
    void duplicada() {
        when(produtos.findByIdAndRestauranteId(2L, 1L)).thenReturn(Optional.of(foto.getProduto()));
        when(fotos.findByProdutoId(2L)).thenReturn(Optional.of(foto));

        var erro = assertThrows(ResponseStatusException.class, () -> service.adicionar(1L, 2L, arquivo, null));

        assertEquals(409, erro.getStatusCode().value());
        verifyNoInteractions(storage);
    }

    @Test
    @DisplayName("Deve rejeitar atualizacao sem foto cadastrada")
    void semFoto() {
        when(produtos.findByIdAndRestauranteId(2L, 1L)).thenReturn(Optional.of(foto.getProduto()));

        assertThrows(ResourceNotFoundException.class, () -> service.atualizar(1L, 2L, arquivo, null));

        verifyNoInteractions(storage);
    }

    @Test
    @DisplayName("Deve rejeitar produto que nao pertence ao restaurante")
    void outroRestaurante() {
        when(produtos.findByIdAndRestauranteId(2L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.adicionar(1L, 2L, arquivo, null));

        verifyNoInteractions(fotos, storage);
    }

    private void prepararAtualizacao() {
        when(produtos.findByIdAndRestauranteId(2L, 1L)).thenReturn(Optional.of(foto.getProduto()));
        when(fotos.findByProdutoId(2L)).thenReturn(Optional.of(foto));
        when(storage.enviar(arquivo)).thenReturn("produtos/nova");
    }

    @Test
    @DisplayName("Deve preservar ambos os arquivos quando o resultado da transacao for desconhecido")
    void resultadoDesconhecido() {
        prepararAtualizacao();
        when(fotos.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.atualizar(1L, 2L, arquivo, null);
        concluir(TransactionSynchronization.STATUS_UNKNOWN);

        verify(storage, never()).excluir(any());
    }

    @Test
    @DisplayName("Deve repetir exclusao sem invalidar a foto confirmada no banco")
    void falhaExclusao() {
        prepararAtualizacao();
        when(fotos.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new ResourceNotFoundException("MinIO indisponivel")).when(storage).excluir("produtos/antiga");

        service.atualizar(1L, 2L, arquivo, null);
        assertDoesNotThrow(() -> concluir(TransactionSynchronization.STATUS_COMMITTED));

        verify(storage, times(3)).excluir("produtos/antiga");
        assertEquals("produtos/nova", foto.getChaveObjeto());
    }

    @Test
    @DisplayName("Deve manter metadados antigos se o upload falhar")
    void falhaUpload() {
        prepararAtualizacao();
        when(storage.enviar(arquivo)).thenThrow(new ResourceNotFoundException("Falha de upload"));

        assertThrows(ResourceNotFoundException.class, () -> service.atualizar(1L, 2L, arquivo, null));

        assertEquals("produtos/antiga", foto.getChaveObjeto());
        verify(fotos, never()).saveAndFlush(any());
        verify(storage, never()).excluir(any());
    }

    private void concluir(int status) {
        TransactionSynchronizationManager.getSynchronizations().forEach(s -> s.afterCompletion(status));
    }
}
