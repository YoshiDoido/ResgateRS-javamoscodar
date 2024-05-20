CREATE DATABASE apoiors;

USE apiors;

CREATE TABLE CentrosDistribuicao (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  endereco VARCHAR(100) NOT NULL,
  cep VARCHAR(10) NOT NULL,
  cidade VARCHAR(20) NOT NULL, 
  capacidade INT NOT NULL,
  quantidade_atual INT NOT NULL
);

INSERT INTO `apoiors`.`CentrosDistribuicao` (`nome`, `endereco`, `cep`, `cidade`, `capacidade`, `quantidade_atual`) VALUES ('CD Esperanca', 'Av. Boqueirao, 2450 - Igara', '92032-420', 'Canoas - RS', '3000', '2500');
INSERT INTO `apoiors`.`CentrosDistribuicao` (`nome`, `endereco`, `cep`, `cidade`, `capacidade`, `quantidade_atual`) VALUES ('CD Prosperidade', 'Av. Borges de Medeiros 1501', '99119900 ', ' Porto Alegre - RS', '3000', '1000');
INSERT INTO `apoiors`.`CentrosDistribuicao` (`nome`, `endereco`, `cep`, `cidade`, `capacidade`, `quantidade_atual`) VALUES ('CD Reconstrucao', ' R. Dr. Décio Martins Costa, 312 - Vila Eunice Nova', '94920-170', 'Cachoeirinha - RS', '3000', '500');

CREATE TABLE Doacoes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(20) NOT NULL,
  nome VARCHAR(20) NOT NULL,
  sexo CHAR,
  tamanho VARCHAR(10),
  quantidade INT NOT NULL,
  centro_distribuicao_id INT NOT NULL,
  FOREIGN KEY (centro_distribuicao_id) REFERENCES CentrosDistribuicao(id)
);

INSERT INTO `apoiors`.`Doacoes` (`tipo`, `nome`, `sexo`, `tamanho`, `quantidade`, `centro_distribuicao_id`) VALUES ('Roupas', 'Agasalho', 'M', 'Infantil', '20', 1);
INSERT INTO `apoiors`.`Doacoes` (`tipo`, `nome`, `sexo`, `tamanho`, `quantidade`, `centro_distribuicao_id`) VALUES ('Roupas', 'Camisas', 'F', 'PP', '20', 1);
INSERT INTO `apoiors`.`Doacoes` (`tipo`, `nome`, `sexo`, `tamanho`, `quantidade`, `centro_distribuicao_id`) VALUES ('Produtos de Higiene', 'Sabonete','500', 2);
INSERT INTO `apoiors`.`Doacoes` (`tipo`, `nome`, `sexo`, `tamanho`, `quantidade`, `centro_distribuicao_id`) VALUES ('Alimentos', 'Arroz','70', 1);


CREATE TABLE Abrigos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  endereco VARCHAR(100) NOT NULL,
  cep VARCHAR(10) NOT NULL,
  cidade VARCHAR(20) NOT NULL,
  responsavel VARCHAR(50) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  email VARCHAR(50) NOT NULL,
  capacidade INT NOT NULL,
  ocupacao DECIMAL(5,2) NOT NULL,
  FOREIGN KEY (id) REFERENCES Doacoes(centro_distribuicao_id)
);

CREATE TABLE Pedidos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  abrigo_id INT NOT NULL,
  item VARCHAR(20) NOT NULL,
  tamanho VARCHAR(10) NOT NULL,
  quantidade INT NOT NULL,
  FOREIGN KEY (abrigo_id) REFERENCES Abrigos(id)
);

CREATE TABLE RequisicaoPedido (
  id INT AUTO_INCREMENT PRIMARY KEY,
  centro_distribuicao_id INT NOT NULL,
  abrigo_id INT NOT NULL,
  item VARCHAR(20) NOT NULL,
  tamanho VARCHAR(10) NOT NULL,
  quantidade INT NOT NULL,
  status ENUM('ACEITO', 'RECUSADO') NOT NULL,
  motivo VARCHAR(100) NOT NULL,
  FOREIGN KEY (centro_distribuicao_id) REFERENCES CentrosDistribuicao(id),
  FOREIGN KEY (abrigo_id) REFERENCES Abrigos(id)
);

CREATE TABLE Transferencias (
  id INT AUTO_INCREMENT PRIMARY KEY,
  centro_distribuicao_origem_id INT NOT NULL,
  centro_distribuicao_destino_id INT NOT NULL,
  item VARCHAR(20) NOT NULL,
  tamanho VARCHAR(10) NOT NULL,
  quantidade INT NOT NULL,
  FOREIGN KEY (centro_distribuicao_origem_id) REFERENCES CentrosDistribuicao(id),
  FOREIGN KEY (centro_distribuicao_destino_id) REFERENCES CentrosDistribuicao(id)
);