# Produtos Favoritos

Indice
=================
<!--ts-->
   * [Features](#Features)
   * [Como_Usar](#Como_Usar)
   * [Endpoints](#Endpoints)
   * [Teste_Performance](#Teste_Performance)
   * [Tecnologias](#Tecnologias)
   * [Arquitetura](#Arquitetura)
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
- [x] Consultar Lista Produtos Favoritos

### Como_Usar

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
- [Java 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html).
- [Docker](https://www.docker.com/products/docker-desktop).
- [Git](https://git-scm.com/downloads).

```
# Clone este repositório
$ git clone https://github.com/heldergomes/produtos-favoritos.git

# Execute o maven install na IDE de sua preferencia

# Acesse o prompt e entre na rota da api
$ cd produtos-favoritos/

# Execute o comando:
$ docker-compose up --build --force-recreate

# Importe a collection e o environment para o postman no pacote: produtos-favoritos/postman/
```

### Endpoints

- Clientes
---
    - CADASTRO CLIENTE
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
    - CONSULTA CLIENTE
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
    - ATUALIZACAO CLIENTE
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
    - DELECAO CLIENTE
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
    - LOGIN CLIENTE
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
    - CADASTRO PRODUTO FAVORITO
    - Uri: /api/v1/clientes/{id}/produtos/{id_produto}
    - Metodo: POST
    - Body
        - status[String - Enum(favorito)]: Status do produto para o cliente
    - Header:
        - Authorization: Token de Acesso do cliente logado
        - X-Correlation-ID: UUID Randomico
    - Response:
        - HttpStatus: 201 [Cadastro realizado com sucesso]
        - HttpStatus: 409 [Cadastro não realizado pois o email ja esta cadastrado]
        - HttpStatus: 404 [Recurso não encontrado]
        - HttpStatus: 403 [Acesso Nao Autorizado]
---
    - CONSULTA LISTA PRODUTOS FAVORITOS DO CLIENTE
    - Uri: /api/v1/clientes/{id}/produtos
    - Metodo: GET
    - Query Params
        - status [Obrigatorio]: Status do produto para o cliente
        - page   [Opcional]:    Numero da pagina da lista 
        - size   [Opcional]:    Quantidade de registros que a pagina deve retornar
    - Header:
        - Authorization: Token de Acesso do cliente logado
        - X-Correlation-ID: UUID Randomico
    - Response:
        - HttpStatus: 200 [Consulta de Lista de produtos favoritos com sucesso]
        - Body:
            - id[String]: Id do Produto
            - image[String]: Url da imagem do produto
            - price[BigDecimal]: Preco do Produto
            - title[String]: Titulo do produto
            - reviewScore[BigDecimal]: Nota do produto
        - HttpStatus: 403 [Acesso Nao Autorizado]
---

### Teste_Performance

```
# Abra pasta JMeter
$ cd /produtos-favoritos/jmeter

# Executar plano de teste e gerar dashboard
$ jmeter -n -t teste_carga.jmx -l resultado.jtl -e -o testes_produtos_favoritos

# Abrir resultado de execução
$ open /produtos-favoritos/jmeter/testes_produtos_favoritos/index.html
```

### Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

- [Java 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html)
- [MongoDb](https://www.mongodb.com)

### Arquitetura

***Conceito dos Componentes***<br>
|- cliente (Componente que possui todo desenvolvimento relacionado ao cliente)<br>
|- config (Componente que possui toda configuracao da aplicacao)<br>
|- exception (Componente que trata toda excessao da aplicacao)<br>
|- produto (Componente que possui todo desenvolvimento relacionado ao produto do cliente)<br>
|- security (Componente que possui toda camada de autenticacao e autorizacao da aplicacao)<br>

***Padrao de Sufixo das classes***<br>
|- * (Classe que representa uma entidade)<br>
|- *Controller (Classe que controla os endpoints de uma entidade)<br>
|- *Dto (Classe que segrega o body da entidade da propria entidade de dominio)<br>
|- *Repository (Classe que persiste todos os dados da entidade)<br>
|- *ApiRequest (Classe que requisita outra aplicacao para a entidade)<br>
|- *UseCase (Classe que representa um caso de uso da entidade)<br>

### Autor

<p>Helder Ardachnikoff Gomes</p>

[![Linkedin Badge](https://img.shields.io/badge/-Helder-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/helder-ardachnikoff-b91b25122/)](https://www.linkedin.com/in/helder-ardachnikoff-b91b25122/) 
[![Gmail Badge](https://img.shields.io/badge/-helder.versatti@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:helder.versatti@gmail.com)](mailto:helder.versatti@gmail.com)
