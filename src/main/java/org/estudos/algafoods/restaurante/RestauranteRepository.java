package org.estudos.algafoods.restaurante;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    Page<Restaurante> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("""
            SELECT r FROM Restaurante r WHERE (:nome IS NULL OR LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
            """)
    Page<Restaurante> buscarRestaurantes(@Param("nome") String nome, Pageable pageable);
}
