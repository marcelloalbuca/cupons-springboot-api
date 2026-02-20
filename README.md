Cupons API 

API REST para gerenciamento de Cupons, com foco em Create e Delete (Soft Delete) respeitando regras de negócio no domínio (DDD).

Projeto desenvolvido em Java 17 + Spring Boot 3, utilizando H2 em memória, documentação Swagger/OpenAPI, testes automatizados e execução via Docker Compose.

Tecnologias utilizadas

Java 17

Spring Boot 3

Spring Web

Spring Validation

Spring Data JPA

H2 Database (em memória)

Swagger / OpenAPI

JUnit 5

Maven

Docker + Docker Compose

Regras de Negócio
CREATE

Um cupom deve possuir:

Campo	Regra
code	obrigatório, alfanumérico com 6 caracteres após sanitização
description	obrigatório
discountValue	mínimo 0.5
expirationDate	não pode ser no passado
published	opcional

Observações:

Caracteres especiais são aceitos na entrada

Porém são removidos antes de salvar e retornar

O resultado final deve possuir exatamente 6 caracteres

Exemplo:

Entrada:

A-1@B#2$C!3

Persistido/Retornado:

A1B2C3
DELETE (Soft Delete)

O cupom não é removido do banco

Apenas marcado como deletado

Não é permitido deletar o mesmo cupom duas vezes

Pré-requisitos

Instale antes de começar:

Java 17

Verifique:

java -version
Maven Wrapper (já incluso no projeto)

Não precisa instalar Maven globalmente

Docker Desktop (opcional)

Para execução containerizada

Rodando o projeto localmente
1) Clonar o repositório
git clone https://github.com/marcelloalbuca/cupons-springboot-api.git
cd cupons
2) Rodar testes
./mvnw clean test
3) Subir aplicação
./mvnw spring-boot:run

Aplicação disponível em:

http://localhost:8080

Swagger:

http://localhost:8080/swagger-ui/index.html

H2 Console:

http://localhost:8080/h2-console

JDBC URL:

jdbc:h2:mem:testdb

User:

sa

Password:

(sem senha)
Rodando com Docker
Requisito

Docker Desktop precisa estar em execução

Verifique:

docker version
Subir aplicação
docker compose up --build

Swagger:

http://localhost:8080/swagger-ui/index.html
Testes manuais via Swagger

Abra:

http://localhost:8080/swagger-ui/index.html
1) Criar cupom válido

Endpoint:
POST /cupons

Body:

{
  "code": "A-1@B#2$C!3",
  "description": "Cupom de teste",
  "discountValue": 1.0,
  "expirationDate": "2030-12-31",
  "published": true
}

Resultado esperado:

Status:

201 Created

Resposta:

{
  "id": "...",
  "code": "A1B2C3",
  "description": "Cupom de teste",
  "discountValue": 1.0,
  "expirationDate": "2030-12-31",
  "published": true
}

Observe que o code foi sanitizado para 6 caracteres.

2) Criar cupom com desconto inválido
{
  "code": "ABC123",
  "description": "Cupom inválido",
  "discountValue": 0.49,
  "expirationDate": "2030-12-31"
}

Resultado esperado:

400 Bad Request
3) Criar cupom com data no passado
{
  "code": "ABC123",
  "description": "Cupom inválido",
  "discountValue": 1.0,
  "expirationDate": "2020-01-01"
}

Resultado esperado:

400 Bad Request
4) Criar cupom com código inválido (menos de 6 após sanitizar)
{
  "code": "A-1@B",
  "description": "Cupom inválido",
  "discountValue": 1.0,
  "expirationDate": "2030-12-31"
}

Resultado esperado:

400 Bad Request
5) Deletar cupom

Use o id retornado no primeiro create:

DELETE /cupons/{id}

Resultado esperado:

204 No Content
6) Deletar o mesmo cupom novamente

Resultado esperado:

409 Conflict
