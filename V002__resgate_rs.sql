# DDL's

CREATE DATABASE if NOT EXISTS resgate_rs;
USE resgate_rs;

CREATE TABLE centro_distribuicao(
	id BIGINT NOT NULL AUTO_INCREMENT,
	nome VARCHAR(100) NOT NULL,
	endereco VARCHAR(300) NOT NULL,
	cep VARCHAR(9) NOT NULL,
	
	PRIMARY KEY(id)
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

# ----------------------

CREATE TABLE armazem (
	id BIGINT NOT NULL,
	
	PRIMARY KEY (id),
	
	FOREIGN KEY (id) REFERENCES centro_distribuicao(id) 
	ON DELETE CASCADE
		
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


# ----------------------

CREATE TABLE produtos (
	id BIGINT NOT NULL AUTO_INCREMENT,
	categoria ENUM('ALIMENTO', 'HIGIENE', 'ROUPA') NOT NULL,
	item ENUM('ARROZ', 'FEIJÃO', 'LEITE', 'SABONETE', 'ESCOVA_DE_DENTES', 'PASTA_DE_DENTES', 'ABSORVENTES', 'AGASALHO', 'CAMISA') NOT NULL,
	sexo ENUM('F', 'M'),
	tamanho ENUM('INFANTIL', 'PP', 'P', 'M', 'G', 'GG'),
	quantidade INT NOT NULL,
	armazem_id BIGINT NOT NULL,
	
	PRIMARY KEY (id),
	FOREIGN KEY (armazem_id) REFERENCES armazem(id) ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4; 

# ----------------------

# DML's


SELECT categoria, SUM(quantidade) AS 'Quantidade Total' FROM produtos GROUP BY categoria;

SELECT p.categoria, SUM(quantidade) AS 'Quantidade Total' FROM centro_distribuicao cd 
INNER JOIN armazem a ON (cd.id = a.id)
INNER JOIN produtos p ON (p.armazem_id = a.id)
WHERE cd.id = 1 GROUP BY categoria;

INSERT INTO centro_distribuicao(nome, endereco, cep) VALUES("Esperança", "Av. Boqueirão, 2450 - Igara, Canoas - RS", "92032-420");

INSERT INTO armazem(id) VALUES(1);

INSERT INTO produtos(armazem_id, categoria, item, quantidade) VALUES(1, "ALIMENTO", "ARROZ", 300);
INSERT INTO produtos(armazem_id, categoria, item, quantidade) VALUES(1, "HIGIENE", "ESCOVA_DE_DENTES", 100);
INSERT INTO produtos(armazem_id, categoria, item, sexo, tamanho, quantidade) VALUES(1, "ROUPA", "AGASALHO", "F", "P", 100);

SELECT p.categoria, p.item, p.quantidade, p.sexo, p.tamanho FROM centro_distribuicao cd 
INNER JOIN armazem a ON (cd.id = a.id)
INNER JOIN produtos p ON (p.armazem_id = a.id)
WHERE cd.id = 1;
