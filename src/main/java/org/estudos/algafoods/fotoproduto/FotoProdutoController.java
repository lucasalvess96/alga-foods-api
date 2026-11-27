package org.estudos.algafoods.fotoproduto;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/restaurantes/{restauranteId}")
public class FotoProdutoController {
    private final FotoProdutoService service;

    public FotoProdutoController(FotoProdutoService service) {
        this.service = service;
    }

    @PostMapping(path = "/salvar/produtos/{produtoId}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoProdutoDto> adicionar(@PathVariable Long restauranteId,
                                                    @PathVariable Long produtoId, @RequestPart("arquivo") MultipartFile arquivo,
                                                    @RequestParam(required = false) String descricao) {
        var foto = service.adicionar(restauranteId, produtoId, arquivo, descricao);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(foto);
    }

    @PutMapping(path = "/atualizar/produtos/{produtoId}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoProdutoDto> atualizar(@PathVariable Long restauranteId,
                                                    @PathVariable Long produtoId, @RequestPart("arquivo") MultipartFile arquivo,
                                                    @RequestParam(required = false) String descricao) {
        return ResponseEntity.ok(service.atualizar(restauranteId, produtoId, arquivo, descricao));
    }
}
