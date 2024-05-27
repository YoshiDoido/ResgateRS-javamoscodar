CREATE DATABASE if NOT EXISTS resgate_rs;
USE resgate_rs;

CREATE TABLE centro_distribuicao(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(300) NOT NULL,
    cep VARCHAR(9) NOT NULL,

    PRIMARY KEY(id)
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


CREATE TABLE abrigos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    endereco VARCHAR(100) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    cidade VARCHAR(20) NOT NULL,
    responsavel VARCHAR(50) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    capacidade INT NOT NULL,
    ocupacao INT NOT NULL

)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


CREATE TABLE armazem (
    id INT AUTO_INCREMENT,
    centro_distribuicao_id INT NULL,
    abrigo_id INT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (centro_distribuicao_id) REFERENCES centro_distribuicao(id) ON DELETE CASCADE,
    FOREIGN KEY (abrigo_id) REFERENCES abrigos(id) ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


CREATE TABLE produtos (
    id INT  NOT NULL AUTO_INCREMENT,
    categoria ENUM('ALIMENTO', 'HIGIENE', 'ROUPA', 'LIMPEZA') NOT NULL,
    item ENUM('ARROZ', 'FEIJAO', 'LEITE', 'AGUA', 'SABONETE', 'ESCOVA_DE_DENTES', 'PASTA_DE_DENTES', 'ABSORVENTE', 'AGASALHO', 'CAMISA', 'ALCOOL', 'AGUA_SANITARIA') NOT NULL,
    sexo ENUM('F', 'M'),
    tamanho ENUM('INFANTIL', 'PP', 'P', 'M', 'G', 'GG'),
    quantidade INT NOT NULL,
    armazem_id INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (armazem_id) REFERENCES armazem(id) ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


CREATE TABLE ordem_pedidos(
    id INT NOT NULL AUTO_INCREMENT,
    centro_distribuicao_id INT NOT NULL,
    centro_distribuicao_envio INT NULL,
    abrigo_id INT NULL,
    categoria ENUM('ALIMENTO', 'HIGIENE', 'ROUPA') NOT NULL,
    item ENUM('ARROZ', 'FEIJAO', 'LEITE', 'SABONETE', 'ESCOVA_DE_DENTES', 'PASTA_DE_DENTES', 'ABSORVENTE', 'AGASALHO', 'CAMISA') NOT NULL,
    quantidade INT NOT NULL,
    status ENUM('ACEITO', 'RECUSADO', 'PENDENTE') NOT NULL,
    motivo VARCHAR(255) NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (centro_distribuicao_id) REFERENCES centro_distribuicao(id) ON DELETE CASCADE,
    FOREIGN KEY (centro_distribuicao_envio) references centro_distribuicao(id) ON DELETE CASCADE,
    FOREIGN KEY (abrigo_id) REFERENCES abrigos(id) ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


INSERT INTO centro_distribuicao (nome, endereco, cep) VALUES
('Centro de Distribuição Esperança', 'Av. Boqueirão, 2450 - Igara, Canoas - RS', '92032-420'),
('Centro de Distribuição Prosperidade', 'Av. Borges de Medeiros 1501, Porto Alegre', '90119-900'),
('Centro de Distribuição Reconstrução', 'R. Dr. Décio Martins Costa, 312 - Vila Eunice Nova, Cachoeirinha - RS', '94920-170');

INSERT INTO armazem (centro_distribuicao_id) VALUES
(1),(2),(3);


INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'M', 50, 1),
('ROUPA', 'AGASALHO', 'F', 'P', 50, 1),
('ROUPA', 'CAMISA', 'M', 'G', 55, 1),
('ROUPA', 'CAMISA', 'F', 'M', 55, 1),
('HIGIENE', 'SABONETE', NULL, NULL, 50, 1),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 50, 1),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 50, 1),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 50, 1),
('ALIMENTO', 'ARROZ', NULL, NULL, 100, 1),
('ALIMENTO', 'FEIJAO', NULL, NULL, 50, 1),
('ALIMENTO', 'AGUA', NULL, NULL, 50, 1),
('LIMPEZA', 'ALCOOL', NULL, NULL, 50, 1),
('LIMPEZA', 'AGUA_SANITARIA', NULL, NULL, 50, 1);

INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'M', 60, 2),
('ROUPA', 'AGASALHO', 'F', 'P', 20, 2),
('ROUPA', 'CAMISA', 'M', 'G', 20, 2),
('ROUPA', 'CAMISA', 'F', 'M', 70, 2),
('HIGIENE', 'SABONETE', NULL, NULL, 100, 2),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 100, 2),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 100, 2),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 50, 2),
('ALIMENTO', 'ARROZ', NULL, NULL, 100, 2),
('ALIMENTO', 'FEIJAO', NULL, NULL, 100, 2),
('ALIMENTO', 'LEITE', NULL, NULL, 200, 2),
('ALIMENTO', 'AGUA', NULL, NULL, 80, 2);

INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'M', 40, 3),
('ROUPA', 'AGASALHO', 'F', 'P', 40, 3),
('ROUPA', 'CAMISA', 'M', 'G', 25, 3),
('ROUPA', 'CAMISA', 'F', 'M', 25, 3),
('HIGIENE', 'SABONETE', NULL, NULL, 120, 3),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 130, 3),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 40, 3),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 30, 3),
('ALIMENTO', 'ARROZ', NULL, NULL, 200, 3),
('ALIMENTO', 'FEIJAO', NULL, NULL, 75, 3),
('ALIMENTO', 'LEITE', NULL, NULL, 200, 3),
('ALIMENTO', 'AGUA', NULL, NULL, 90, 3),
('LIMPEZA', 'ALCOOL', NULL, NULL, 100, 3),
('LIMPEZA', 'AGUA_SANITARIA', NULL, NULL, 8, 3);


INSERT INTO abrigos (nome, endereco, cep, cidade, responsavel, telefone, email, capacidade, ocupacao) VALUES
('Abrigo Prosperidade', 'Av. Borges de Medeiros, 1600', '90119-901', 'Viamão', 'Gabriel Pereira', '51 99999-2222', 'gabriel@prosperidade.org', 600, 20),
('Abrigo Estrela', 'R. Saldanha Marinho, 1800', '90010-200', 'Porto Alegre', 'Bruna Rocha', '51 99999-9999', 'bruna@estrela.org', 600, 80),
('Abrigo Reconstrução', 'R. Dr. Décio Martins Costa, 320 - Vila Eunice Nova', '94920-171', 'Cachoeirinha', 'Victor Souza', '51 99999-3333', 'victor@reconstrucao.org', 600, 65),
('Abrigo Vida Nova', 'R. Voluntários da Pátria, 1500', '90230-010', 'Porto Alegre', 'Jhonatan Silva', '51 99999-6666', 'jhonatan@vidanova.org', 600, 90),
('Abrigo Bom Pastor', 'Av. Padre Cacique, 5000', '90830-013', 'Novo Hamburgo', 'Matheus Carvalho', '51 99999-4445', 'matheus@bompastor.org', 600, 72);

INSERT INTO armazem (abrigo_id) VALUES
(1),(2),(3),(4),(5);


INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'G', 20, 4),
('ROUPA', 'AGASALHO', 'F', 'P', 20, 4),
('ROUPA', 'CAMISA', 'M', 'G', 10, 4),
('ROUPA', 'CAMISA', 'F', 'M', 10, 4),
('HIGIENE', 'SABONETE', NULL, NULL, 20, 4),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 15, 4),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 15, 4),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 10, 4),
('ALIMENTO', 'ARROZ', NULL, NULL, 20, 4),
('ALIMENTO', 'FEIJAO', NULL, NULL, 20, 4),
('ALIMENTO', 'LEITE', NULL, NULL, 20, 4),
('ALIMENTO', 'AGUA', NULL, NULL, 25, 4),
('LIMPEZA', 'ALCOOL', NULL, NULL, 5, 4),
('LIMPEZA', 'AGUA_SANITARIA', NULL, NULL, 15, 4);

INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'P', 30, 5),
('ROUPA', 'AGASALHO', 'F', 'P', 30, 5),
('ROUPA', 'CAMISA', 'M', 'G', 10, 5),
('ROUPA', 'CAMISA', 'F', 'M', 10, 5),
('HIGIENE', 'SABONETE', NULL, NULL, 30, 5),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 20, 5),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 20, 5),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 10, 5),
('ALIMENTO', 'ARROZ', NULL, NULL, 30, 5),
('ALIMENTO', 'FEIJAO', NULL, NULL, 25, 5),
('ALIMENTO', 'LEITE', NULL, NULL, 25, 5),
('ALIMENTO', 'AGUA', NULL, NULL, 75, 5),
('LIMPEZA', 'ALCOOL', NULL, NULL, 10, 5),
('LIMPEZA', 'AGUA_SANITARIA', NULL, NULL, 2, 5);

INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'M', 15, 6),
('ROUPA', 'AGASALHO', 'F', 'P', 15, 6),
('ROUPA', 'CAMISA', 'M', 'P', 10, 6),
('ROUPA', 'CAMISA', 'F', 'M', 10, 6),
('HIGIENE', 'SABONETE', NULL, NULL, 15, 6),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 10, 6),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 10, 6),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 15, 6),
('ALIMENTO', 'ARROZ', NULL, NULL, 20, 6),
('ALIMENTO', 'FEIJAO', NULL, NULL, 15, 6),
('ALIMENTO', 'LEITE', NULL, NULL, 15, 6),
('ALIMENTO', 'AGUA', NULL, NULL, 82, 6),
('LIMPEZA', 'ALCOOL', NULL, NULL, 7, 6);


INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'M', 30, 7),
('ROUPA', 'AGASALHO', 'F', 'P', 30, 7),
('ROUPA', 'CAMISA', 'M', 'G', 10, 7),
('ROUPA', 'CAMISA', 'F', 'P', 10, 7),
('HIGIENE', 'SABONETE', NULL, NULL, 30, 7),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 20, 7),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 20, 7),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 10, 7),
('ALIMENTO', 'ARROZ', NULL, NULL, 30, 7),
('ALIMENTO', 'FEIJAO', NULL, NULL, 25, 7),
('ALIMENTO', 'LEITE', NULL, NULL, 25, 7),
('ALIMENTO', 'AGUA', NULL, NULL, 76, 7),
('LIMPEZA', 'ALCOOL', NULL, NULL, 20, 7),
('LIMPEZA', 'AGUA_SANITARIA', NULL, NULL, 3, 7);


INSERT INTO produtos (categoria, item, sexo, tamanho, quantidade, armazem_id) VALUES
('ROUPA', 'AGASALHO', 'M', 'M', 15, 8),
('ROUPA', 'AGASALHO', 'F', 'P', 15, 8),
('ROUPA', 'CAMISA', 'M', 'P', 10, 8),
('ROUPA', 'CAMISA', 'F', 'M', 10, 8),
('HIGIENE', 'SABONETE', NULL, NULL, 15, 8),
('HIGIENE', 'ESCOVA_DE_DENTES', NULL, NULL, 10, 8),
('HIGIENE', 'PASTA_DE_DENTES', NULL, NULL, 10, 8),
('HIGIENE', 'ABSORVENTE', NULL, NULL, 15, 8),
('ALIMENTO', 'ARROZ', NULL, NULL, 20, 8),
('ALIMENTO', 'FEIJAO', NULL, NULL, 15, 8),
('ALIMENTO', 'LEITE', NULL, NULL, 15, 8),
('ALIMENTO', 'AGUA', NULL, NULL, 82, 8),
('LIMPEZA', 'ALCOOL', NULL, NULL, 7, 8);