
CREATE TABLE IF NOT EXISTS cliente (
   cliente_id VARCHAR UNIQUE NOT NULL,
   nome VARCHAR NOT NULL,
   email VARCHAR UNIQUE NOT NULL,
   PRIMARY KEY (cliente_id)
);

CREATE TABLE IF NOT EXISTS produtos_favoritos (
   produto_id VARCHAR NOT NULL,
   cliente_id VARCHAR NOT NULL,
   image VARCHAR NOT NULL,
   price DECIMAL NOT NULL,
   title VARCHAR NOT NULL,
   review_score DECIMAL,
   PRIMARY KEY (produto_id, cliente_id),
   FOREIGN KEY (cliente_id)
        REFERENCES cliente (cliente_id)
);