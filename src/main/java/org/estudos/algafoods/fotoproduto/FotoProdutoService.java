package org.estudos.algafoods.fotoproduto;

import org.estudos.algafoods.excecao.ResourceNotFoundException;
import org.estudos.algafoods.minio.MinioStorageService;
import org.estudos.algafoods.produto.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoProdutoService {

    private static final Logger log = LoggerFactory.getLogger(FotoProdutoService.class);

    private final ProdutoRepository produtos;
    private final FotoProdutoRepository fotos;
    private final MinioStorageService storage;

    public FotoProdutoService(ProdutoRepository produtos, FotoProdutoRepository fotos, MinioStorageService storage) {
        this.produtos = produtos;
        this.fotos = fotos;
        this.storage = storage;
    }

    @Transactional
    public FotoProdutoDto adicionar(Long restauranteId, Long produtoId, MultipartFile arquivo, String descricao) {
        return salvar(restauranteId, produtoId, arquivo, descricao, false);
    }

    @Transactional
    public FotoProdutoDto atualizar(Long restauranteId, Long produtoId, MultipartFile arquivo, String descricao) {
        return salvar(restauranteId, produtoId, arquivo, descricao, true);
    }

    private FotoProdutoDto salvar(Long restauranteId, Long produtoId, MultipartFile arquivo, String descricao, boolean atualizar) {
        var produto = produtos.findByIdAndRestauranteId(produtoId, restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado no restaurante"));

        var existente = fotos.findByProdutoId(produtoId);

        if (arquivo == null || arquivo.isEmpty()) {
            throw new ResourceNotFoundException("O arquivo deve conter dados");
        }

        String nome = arquivo.getOriginalFilename();
        if (nome == null || nome.isBlank() || nome.length() > 255) {
            throw new ResourceNotFoundException("Informe um nome de arquivo de ate 255 caracteres");
        }

        var foto = existente.orElseGet(FotoProduto::new);
        String anterior = foto.getChaveObjeto();
        String chave = storage.enviar(arquivo);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_UNKNOWN) {
                    log.error("Resultado da transacao desconhecido. Preservados os objetos novo {} e anterior {} para reconciliacao", chave, anterior);
                    return;
                }
                String remover = status == STATUS_COMMITTED ? anterior : chave;
                if (remover != null) {
                    for (int tentativa = 1; tentativa <= 3; tentativa++) {
                        try {
                            storage.excluir(remover);
                            break;
                        } catch (RuntimeException exception) {
                            if (tentativa == 3) {
                                log.error("Falha ao limpar objeto MinIO {} apos 3 tentativas. Necessaria reconciliacao", remover, exception);
                            }
                        }
                    }
                }
            }
        });
        foto.setProduto(produto);
        foto.setNome(nome);
        foto.setDescricao(descricao);
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setChaveObjeto(chave);
        return FotoProdutoConverter.toFotoProdutoDto(fotos.saveAndFlush(foto));
    }
}
