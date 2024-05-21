package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.CentroDistribuicaoDAO;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Produto;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CentroDistribuicaoDAOImpl implements CentroDistribuicaoDAO {

    @Override
    public List<CentroDistribuicao> findAll() {
        String sql = "SELECT * FROM centro_distribuicao";
        List<CentroDistribuicao> centrosDistribuicao = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    var centroDistribuicao = new CentroDistribuicao();
                    centroDistribuicao.setId(rs.getInt("id"));
                    centroDistribuicao.setNome(rs.getString("nome"));
                    centroDistribuicao.setCep(rs.getString("cep"));
                    centroDistribuicao.setEndereco(rs.getString("endereco"));

                    centrosDistribuicao.add(centroDistribuicao);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todos centros de distribuição", e);
        }

        return centrosDistribuicao;
    }

    @Override
    public Optional<CentroDistribuicao> findById(Integer entityId) {
        CentroDistribuicao centroDistribuicao = null;
        String sql = "SELECT * FROM centro_distribuicao WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entityId);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    centroDistribuicao = new CentroDistribuicao();
                    centroDistribuicao.setId(rs.getInt("id"));
                    centroDistribuicao.setNome(rs.getString("nome"));
                    centroDistribuicao.setCep(rs.getString("cep"));
                    centroDistribuicao.setEndereco(rs.getString("endereco"));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar centro de distribuição", e);
        }

        return Optional.ofNullable(centroDistribuicao);
    }

    @Override
    public <S extends CentroDistribuicao> S save(S entity) {
        String sql = "INSERT INTO centro_distribuicao(nome, endereco, cep) VALUES(?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getNome());
                ps.setString(2, entity.getEndereco());
                ps.setString(3, entity.getCep());
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idCriado = rs.getInt(1);
                        entity.setId(idCriado);
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao salvar centro de distribuição", e);
        }

        return entity;
    }

    @Override
    public <S extends CentroDistribuicao> S update(Integer entityId, S entity) {
        String sql = "UPDATE centro_distribuicao SET nome = ?, endereco = ?, cep = ? WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getNome());
                ps.setString(2, entity.getEndereco());
                ps.setString(3, entity.getCep());
                ps.setInt(4, entityId);
                ps.executeUpdate();

                entity.setId(entityId);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao atualizar centro de distribuição", e);
        }
        return entity;
    }

    @Override
    public void deleteById(Integer entityId) {
        String sql = "DELETE FROM centro_distribuicao WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, entityId);
                ps.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao apagar centro de distribuição", e);
        }
    }

    @Override
    public Produto inserirDoacao(Integer id, Produto produto) {
        // Query para caso o produto seja da categoria HIGIENE ou ALIMENTO
        String sql = "INSERT INTO produtos(centro_distribuicao_id, categoria, item, quantidade) VALUES(?, ?, ?, ?)";
        int totalDeSets = 4;
        if (produto.getCategoria().equals(Produto.Categoria.ROUPA)) {
            // Query para caso o produto seja da categoria roupa. Deixar as colunas 'sexo' e 'tamanho' por últlimo.
            totalDeSets = 6;
            sql = "INSERT INTO produtos(centro_distribuicao_id, categoria, item, sexo, tamanho, quantidade) VALUES(?, ?, ?, ?, ?, ?)";
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, produto.getCentroDistribuicaoId());
                ps.setString(2, produto.getCategoria().name());
                ps.setString(3, produto.getItem().name());
                if (produto.getCategoria().equals(Produto.Categoria.ROUPA)) {
                    ps.setString(4, produto.getSexo().name());
                    ps.setString(5, produto.getTamanho().name());
                }
                ps.setInt(totalDeSets, produto.getQuantidade());
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idProdutoCriado = rs.getInt(1);
                        produto.setId(idProdutoCriado);
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao inserir um produto ao centro de distribuicao", e);
        }

        return produto;
    }

    @Override
    public List<Produto> findAllDoacoes(Integer id) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.* FROM centro_distribuicao cd " + "INNER JOIN produtos p " +
                "ON (p.centro_distribuicao_id = cd.id) " +
                "WHERE cd.id = ?;";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setCentroDistribuicaoId(rs.getInt("centro_distribuicao_id"));
                    produto.setCategoria(Produto.Categoria.valueOf(rs.getString("categoria")));
                    produto.setItem(Produto.Item.valueOf(rs.getString("item")));
                    if (produto.getCategoria().equals(Produto.Categoria.ROUPA)) {
                        produto.setSexo(Produto.Sexo.valueOf(rs.getString("sexo")));
                        produto.setTamanho(Produto.Tamanho.valueOf(rs.getString("tamanho")));
                    }
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produtos.add(produto);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todos produtos do centro de distribuição", e);
        }

        return produtos;
    }

    @Override
    public Map<Produto.Categoria, Integer> totalDoacoes(Integer id) {
        Map<Produto.Categoria, Integer> categoriaQuantidade = new HashMap<>();
        String sql = "SELECT p.categoria, SUM(quantidade) AS 'quantidade' FROM centro_distribuicao cd " +
                "INNER JOIN produtos p ON (p.centro_distribuicao_id = cd.id) " +
                "WHERE cd.id = ? GROUP BY categoria;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categoriaQuantidade.put(
                            Produto.Categoria.valueOf(rs.getString("categoria")),
                            rs.getInt("quantidade")
                    );
                }
            }
        } catch (SQLException e) {

        }

        return categoriaQuantidade;
    }


}