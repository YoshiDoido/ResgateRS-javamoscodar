package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.CentroDistribuicaoDAO;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Doacao;
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
    public Doacao inserirDoacao(Integer id, Doacao doacao) {
        // Query para caso o produto seja da categoria HIGIENE ou ALIMENTO
        String sql = "INSERT INTO produtos(armazem_id, categoria, item, quantidade) VALUES(?, ?, ?, ?)";
        int totalDeSets = 4;
        if (doacao.getCategoria().equals(Doacao.Categoria.ROUPA)) {
            // Query para caso o produto seja da categoria roupa. Deixar as colunas 'sexo' e 'tamanho' por últlimo.
            totalDeSets = 6;
            sql = "INSERT INTO produtos(armazem_id, categoria, item, sexo, tamanho, quantidade) VALUES(?, ?, ?, ?, ?, ?)";
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, doacao.getArmazemId());
                ps.setString(2, doacao.getCategoria().name());
                ps.setString(3, doacao.getItem().name());
                if (doacao.getCategoria().equals(Doacao.Categoria.ROUPA)) {
                    ps.setString(4, doacao.getSexo().name());
                    ps.setString(5, doacao.getTamanho().name());
                }
                ps.setInt(totalDeSets, doacao.getQuantidade());
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idProdutoCriado = rs.getInt(1);
                        doacao.setId(idProdutoCriado);
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

        return doacao;
    }

    @Override
    public List<Doacao> findAllDoacoes(Integer id) {
        List<Doacao> doacaos = new ArrayList<>();
        String sql = "SELECT p.* FROM centro_distribuicao cd INNER JOIN armazem a ON (a.centro_distribuicao_id = cd.id)" +
                " INNER JOIN produtos p ON (p.armazem_id = a.id) WHERE cd.id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var produto = new Doacao();
                    produto.setId(rs.getInt("id"));
                    produto.setArmazemId(rs.getInt("armazem_id"));
                    produto.setCategoria(Doacao.Categoria.valueOf(rs.getString("categoria")));
                    produto.setItem(Doacao.Item.valueOf(rs.getString("item")));
                    if (produto.getCategoria().equals(Doacao.Categoria.ROUPA)) {
                        produto.setSexo(Doacao.Sexo.valueOf(rs.getString("sexo")));
                        produto.setTamanho(Doacao.Tamanho.valueOf(rs.getString("tamanho")));
                    }
                    produto.setQuantidade(rs.getInt("quantidade"));
                    doacaos.add(produto);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todos produtos do centro de distribuição", e);
        }

        return doacaos;
    }

    @Override
    public Map<Doacao.Categoria, Integer> totalDoacoes(Integer id) {
        Map<Doacao.Categoria, Integer> categoriaQuantidade = new HashMap<>();
        String sql = "SELECT p.categoria, SUM(quantidade) AS 'quantidade' FROM centro_distribuicao cd " +
                "INNER JOIN armazem a ON (a.centro_distribuicao_id = cd.id) " +
                "INNER JOIN produtos p ON (p.armazem_id = a.id) " +
                "WHERE cd.id = ? GROUP BY categoria";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categoriaQuantidade.put(
                            Doacao.Categoria.valueOf(rs.getString("categoria")),
                            rs.getInt("quantidade")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falaha ao recuperar todas doações do centro de distribuição", e);
        }

        return categoriaQuantidade;
    }

    @Override
    public Integer getArmazemId(Integer id) {
        Integer armazemId = null;
        String sql = "SELECT a.id FROM centro_distribuicao cd " +
                "INNER JOIN armazem a ON (a.centro_distribuicao_id = cd.id) where cd.id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    armazemId = rs.getInt( "id");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar id do armazem", e);
        }
        return armazemId;
    }


}
