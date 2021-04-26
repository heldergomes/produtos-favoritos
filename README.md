# Produtos Favoritos

Indice
=================
<!--ts-->
   * [Features](#Features)
   * [Como_Usar](#Como_Usar)
   * [Endpoints](#Endpoints)
   * [Tecnologias](#Tecnologias)
   * [Autor](#Autor)
<!--te-->

### Features

- [x] Criar Cliente 
  - Nao posso ter dois clientes com o mesmo e-mail 
- [x] Consultar Cliente
- [x] Atualizar Cliente
- [x] Deletar Cliente
- [x] Autenticacao Cliente
- [x] Autorizacao Cliente
- [x] Criar Produto Favorito
  - Adicionar a lista de produtos favoritos apenas produtos cadastrados
  - Nao deve haver dois produtos iguais na lista de produtos favoritos
- [ ] Consultar Lista Produtos Favoritos

### Como_Usar

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
- [Java 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html).
- [Docker](https://www.docker.com/products/docker-desktop).
- [Git](https://git-scm.com/downloads).

```
# Clone este repositório
$ git clone https://github.com/heldergomes/produtos-favoritos.git

# Acesse o prompt e entre na rota da api
$ cd produtos-favoritos/

# Execute o comando:
$ docker-compose up --build --force-recreate
```

### Endpoints

- Clientes
---
    - Uri: /api/v1/clientes
    - Metodo: POST
    - Header:
        - Content-Type: application/json
        - X-Correlation-ID: UUID Randomico
    - Body:
        - nome[String]: Nome do cliente
        - email[String]: Email do cliente
    - Response:
        - HttpStatus: 201 [Cadastro realizado com sucesso]
        - Header:
            - Location: url/api/v1/clientes/{id}
        - HttpStatus: 409 [Cadastro não realizado pois o email ja esta cadastrado]
        - HttpStatus: 400 [Payload incorreto]
---
    - Uri: /api/v1/clientes/{id}
    - Metodo: GET
    - Header:
        - Authorization: Token de Acesso do cliente logado
        - X-Correlation-ID: UUID Randomico
    - Response:
        - HttpStatus: 200 [Consulta realizada com sucesso]
        - Body:
            - id[String]: id do cliente
            - nome[String]: Nome do cliente
            - email[String]: Email do cliente
        - HttpStatus: 403 [Acesso Nao Autorizado]
        - HttpStatus: 404 [Recurso não encontrado]
---
    - Uri: /api/v1/clientes/{id}
    - Metodo: PUT
    - Header:
        - Content-Type: application/json
        - Authorization: Token de Acesso do cliente logado
        - X-Correlation-ID: UUID Randomico
    - Response:
        - HttpStatus: 200 [Atualizacao realizada com sucesso]
        - Body:
            - nome[String]: Nome do cliente
            - email[String]: Email do cliente
        - HttpStatus: 403 [Acesso Nao Autorizado]
        - HttpStatus: 400 [Payload incorreto]
---
    - Uri: /api/v1/clientes/{id}
    - Metodo: DELETE
    - Header:
        - Authorization: Token de Acesso do cliente logado
        - X-Correlation-ID: UUID Randomico
    - Response:
        - HttpStatus: 200 [Delecao realizada com sucesso]
        - HttpStatus: 403 [Acesso Nao Autorizado]
        - HttpStatus: 404 [Recurso não encontrado]
---
    - Uri: /login
    - Metodo: POST
    - Header:
        - Content-Type: application/json
        - X-Correlation-ID: UUID Randomico
    - Body:
        - nome[String]: Nome do cliente
        - email[String]: Email do cliente
    - Response:
        - HttpStatus: 200 [Login Realizado com sucesso]
        - Body:
            - id[String]: id do cliente
        - Header:
            - Authorization[String]: Token de acesso
        - HttpStatus: 401 [Login nao autorizado]
---

- Produtos Favoritos
---
    - Uri: /api/v1/clientes/{id}/produtosfavoritos/{id_produto}
    - Metodo: POST
    - Header:
        - Authorization: Token de Acesso do cliente logado
        - X-Correlation-ID: UUID Randomico
    - Response:
        - HttpStatus: 201 [Cadastro realizado com sucesso]
        - HttpStatus: 409 [Cadastro não realizado pois o email ja esta cadastrado]
        - HttpStatus: 404 [Recurso não encontrado]
---

### Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

- [Java 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html)
- [Postgresql](https://www.postgresql.org)

### Autor

<p>Helder Ardachnikoff Gomes</p>

[![Linkedin Badge](https://img.shields.io/badge/-Helder-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/helder-ardachnikoff-b91b25122/)](https://www.linkedin.com/in/helder-ardachnikoff-b91b25122/) 
[![Gmail Badge](https://img.shields.io/badge/-helder.versatti@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:helder.versatti@gmail.com)](mailto:helder.versatti@gmail.com)
