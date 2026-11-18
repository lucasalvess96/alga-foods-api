# Migrations do banco existente

O banco AlgaFoods existente representa a versao 1. Nao existe script V1:
o baseline apenas registra esse estado, sem recriar tabelas ou alterar dados.
Esta configuracao nao provisiona um banco vazio.

Na primeira inicializacao de cada banco existente, defina temporariamente
FLYWAY_BASELINE_ON_MIGRATE=true. Apos o baseline, remova essa variavel.
O historico fica em foods.flyway_schema_history. Os schemas foods e dbo
sao considerados na verificacao do banco existente.

As proximas alteracoes devem ser scripts SQL nesta pasta, iniciando em
V2__descricao_da_alteracao.sql. Use nomes de tabelas qualificados com schema,
por exemplo foods.restaurante ou dbo.SPRING_SESSION (confira o schema real
das tabelas de sessao antes de criar uma migration para elas).

Nao altere migrations ja executadas: crie uma nova versao.
Hibernate valida as entidades, mas nao altera tabelas. As tabelas de sessao
tambem devem existir previamente; sua criacao automatica esta desabilitada.
