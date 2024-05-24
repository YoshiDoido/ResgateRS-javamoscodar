<h1 align="center"> 🤝 Resgate RS 🤝 </h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-v0.1-blue.svg?cacheSeconds=2592000" />
  <a href="https://github.com/YoshiDoido/ResgateRS-javamoscodar" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
</p>

## Sobre o Projeto

> O projeto consiste no desenvolvimento de uma estrutura para Backend que compreenda em uma aplicação para auxílio à desabrigados das enchentes, utilizando as tecnologias e conhecimentos aprendidos no curso.

### 🏠 [Página Inicial](https://github.com/YoshiDoido/ResgateRS-javamoscodar)

![image](https://github.com/YoshiDoido/ResgateRS-javamoscodar/assets/89564433/e2c83857-d509-4c3b-a71e-d8c2b5ad2241)

## Índice

- [Features](#features)
- [Como Usar](#como-usar)
- [Autores](#autores)

## [Features](#features)

1. **Registro de Doações**:
    - Cada Centro de Distribuição tem a função de cadastrar, inserir e excluir itens doados.
2. **Registro de Abrigos**:
    - Possibilidade de cadastrar, ler, editar e exluir um abrigo.
3. **Ordem de Pedido**
    - Abrigos podem listar suas necessidades, e a aplicação indicará em quais
      centros de distribuição os itens podem ser encontrados.
    - Abrigos podem enviar uma ordem de pedido para um ou mais centros de distribuição,
      conforme a necessidade.
4. **Checkout de Itens**
    - Cada centro de distribuição tem uma lista de pedidos para aceitar ou recusar.
    - Caso o pedido seja aceito, o centro enviará a quantidade solicitada para o abrigo.
5. **Transferência de Doações**
    - Transferência de itens entre centros de distribuição ou devolução de itens para um abrigo em caso de excedente, atualizando os totais de cada item em cada local.

## [Como Usar](#como-usar)

Para instalar e rodar esse projeto de forma local, siga os seguintes passos:

1. Clone o repositório na sua máquina local.
   ```bash
   git clone https://github.com/YoshiDoido/ResgateRS-javamoscodar.git
   ```

2. Navegue até o diretório do projeto.
   ```bash
   cd ResgateRS-javamoscodar
   ```

3. Crie um novo banco de dados para o projeto.
    1. Dentro do diretório do projeto, na pasta `resources/database` (`src/main/resources/database`) existe um arquivo de nome `resgate_rs.sql`
    2. Copie esse arquivo e crie um novo banco de dados MySQL.

4. Conexão com o banco de dados MySQL.
    1. Dentro do diretório do projeto na pasta `resources` (`src/main/resources)` crie um arquivo de nome `database.properties`
        - ![image](https://github.com/YoshiDoido/ResgateRS-javamoscodar/assets/89564433/22f1588d-7f07-44ff-b325-88b9ed9971d3)
    2. Dentro do arquivo `database.properties` configure sua autenticação MySQL
   ```bash
   db.url=jdbc:mysql://localhost:3306/resgate_rs?useTimezone=true&serverTimezone=UTC
   db.user=seu_usuario_aqui
   db.password=sua_senha_aqui
   ```


5. Inicie o projeto.
    1. Rode o método estático `main()` da classe `Main` (`src/main/java/Main`).


## [Autores](#autores)

- 👤 **Bruna Leticia dos Santos** - **[Github](https://github.com/Bruna-Leticia12)**
- 👤 **Gabriel Yoshino** - **[Github](https://github.com/jhonatanfelix20)**
- 👤 **Jhonatan Viana Felix** - **[Github](https://github.com/YoshiDoido)**
- 👤 **Matheus Esposto** - **[Github](https://github.com/EternalGerms)**
- 👤 **Victor Miralhas** -  **[Github](https://github.com/Miralhas)**
