# Integracoes do restaurante

POST `/api/public/restaurantes/create` continua aceitando o cadastro anterior.
Os novos campos sao opcionais. Exemplo com todas as novas associacoes:

```json
{
  "nome": "Comida boa",
  "taxaFrete": 10.99,
  "ativo": true,
  "cozinha": { "nome": "Brasileira" },
  "endereco": {
    "cep": "01001000",
    "logradouro": "Rua A",
    "numero": "10",
    "complemento": null,
    "bairro": "Centro"
  },
  "produtos": [
    { "nome": "Marmita", "descricao": "Porcao individual", "preco": 25.00, "ativo": true }
  ],
  "formasPagamento": [1],
  "responsaveis": [2]
}
```

Os pagamentos e usuarios devem existir no banco; IDs inexistentes retornam 404.
Endereco e produtos sao criados junto com o restaurante. Pagamentos e usuarios
sao compartilhados e nao recebem cascade de exclusao do restaurante.

Pagamento continua sendo o nome da entidade de formas de pagamento. Endereco
continua como entidade em sua propria tabela, relacionado por endereco_id.
Produto e Pedido recebem restaurante_id. O vinculo do pedido e feito por
Pedido.setRestaurante; pedidos nao sao criados no cadastro de restaurante.

Com ddl-auto: update, o Hibernate aplica as novas estruturas ao iniciar a API.
As referencias novas sao opcionais para preservar registros ja existentes.
Nao houve migracao nem preenchimento automatico de registros antigos.

A seguranca permanece igual: utilizar a mesma sessao e o token CSRF no POST.

Validacao: compilacao e testes unitarios RestauranteIntegracaoTest, cobrindo
conversao dos produtos/endereco, associacao de IDs existentes, deduplicacao
e rejeicao de pagamento inexistente. Persistencia real no SQL Server nao foi
executada nesses testes.
