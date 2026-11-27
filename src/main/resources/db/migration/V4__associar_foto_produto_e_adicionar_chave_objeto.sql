-- Fotos existentes precisam de uma associacao e uma chave reais antes desta alteracao.
-- Nao gerar referencias ficticias nem excluir os registros automaticamente.
IF EXISTS (SELECT 1 FROM foods.foto_produto)
BEGIN
    THROW 50001, 'foto_produto possui registros. Prepare uma migracao dos dados com produto_id e chave_objeto reais antes de aplicar a V4.', 1;
END;

ALTER TABLE foods.foto_produto
    ADD produto_id BIGINT NOT NULL,
        chave_objeto VARCHAR(512) NOT NULL;

ALTER TABLE foods.foto_produto
    ADD CONSTRAINT FK_foto_produto_produto
        FOREIGN KEY (produto_id) REFERENCES foods.produto (id),
        CONSTRAINT UQ_foto_produto_produto UNIQUE (produto_id);
