package org.estudos.algafoods.fotoproduto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FotoProdutoRepository extends JpaRepository<FotoProduto, Long> {

    Optional<FotoProduto> findByProdutoId(Long produtoId);
}
