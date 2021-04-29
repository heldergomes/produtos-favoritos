
CREATE TABLE IF NOT EXISTS cliente (
   cliente_id VARCHAR UNIQUE NOT NULL,
   nome VARCHAR NOT NULL,
   email VARCHAR UNIQUE NOT NULL,
   PRIMARY KEY (cliente_id)
);

CREATE TABLE IF NOT EXISTS produtos (
   produto_id VARCHAR NOT NULL,
   cliente_id VARCHAR NOT NULL,
   image VARCHAR NOT NULL,
   price DECIMAL NOT NULL,
   title VARCHAR NOT NULL,
   review_score DECIMAL,
   PRIMARY KEY (produto_id, cliente_id),
   FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id)
);

CREATE TABLE IF NOT EXISTS dominio_status_produto (
   status_produto VARCHAR NOT NULL,
   PRIMARY KEY (status_produto)
);

CREATE TABLE IF NOT EXISTS status_produto (
   status_produto VARCHAR NOT NULL,
   produto_id VARCHAR NOT NULL,
   cliente_id VARCHAR NOT NULL,
   PRIMARY KEY (status_produto, produto_id, cliente_id),
   FOREIGN KEY (produto_id, cliente_id)     REFERENCES produtos (produto_id, cliente_id) ON UPDATE CASCADE,
   FOREIGN KEY (status_produto) REFERENCES dominio_status_produto (status_produto) ON UPDATE CASCADE
);
