package org.estudos.algafoods.restaurante;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin
public class RestauranteController implements RestauranteSwagger {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping("/create")
    @Transactional
    @Override
    public ResponseEntity<RestauranteDto> create(@RequestBody @Valid RestauranteDto restauranteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteService.create(restauranteDto));
    }

    @GetMapping("/listar")
    @Override
    public ResponseEntity<List<RestauranteDto>> listarCozinhas() {
        return ResponseEntity.ok(restauranteService.listar());
    }

    @GetMapping("/pagination")
    @Override
    public ResponseEntity<Page<RestauranteDto>> pagination(Pageable pageable) {
        return ResponseEntity.ok(restauranteService.pagination(pageable));
    }

    @GetMapping("/detail/{id}")
    @Override
    public ResponseEntity<RestauranteDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.detail(id));
    }

    @GetMapping("/search")
    @Override
    public ResponseEntity<List<RestauranteDto>> searchList(@RequestParam String nome) {
        return ResponseEntity.ok(restauranteService.search(nome));
    }

    @GetMapping("/search/pagination")
    @Override
    public ResponseEntity<Page<RestauranteDto>> searchPagination(@RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(restauranteService.searchPagination(nome, pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<RestauranteDto>> restaurante(
            @ModelAttribute RestauranteFiltro filtro,
            @PageableDefault(sort = {"dataCadastro"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(restauranteService.restaurantes(filtro, pageable));
    }
}
