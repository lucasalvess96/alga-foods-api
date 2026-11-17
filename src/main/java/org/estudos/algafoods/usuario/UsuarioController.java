package org.estudos.algafoods.usuario;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class UsuarioController implements UsuarioSwagger {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    @Override
    public ResponseEntity<UsuarioDto> criar(@RequestBody @Valid UsuarioDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto));
    }

    @GetMapping("/{usuarioId}")
    @Override
    public UsuarioDto consultar(@PathVariable Long usuarioId) {
        return usuarioService.consultar(usuarioId);
    }

    @GetMapping("/api/restaurantes/{restauranteId}/responsaveis")
    @Override
    public List<UsuarioDto> listarResponsaveis(@PathVariable Long restauranteId) {
        return usuarioService.listarResponsaveis(restauranteId);
    }

    @PutMapping("/api/restaurantes/{restauranteId}/responsaveis/{usuarioId}")
    @Override
    public ResponseEntity<Void> associarResponsavel(@PathVariable Long restauranteId,
                                                    @PathVariable Long usuarioId) {
        usuarioService.associarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/restaurantes/{restauranteId}/responsaveis/{usuarioId}")
    @Override
    public ResponseEntity<Void> removerResponsavel(@PathVariable Long restauranteId,
                                                   @PathVariable Long usuarioId) {
        usuarioService.removerResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
