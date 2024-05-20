package uol.compass.infrastructure.repository;

import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.repository.CentroDistribuicaoRepository;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CentroDistribuicaoRepositoryImpl implements CentroDistribuicaoRepository {

    @Override
    public List<CentroDistribuicao> findAll() {
        String sql = "SELECT * FROM centro_distribuicao";
//        System.out.printf("Query: %s\n", sql);
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
        String sql = "SELECT * FROM centro_distribuicao WHERE id = ?";
//        System.out.printf("Query: %s\n", sql);
        CentroDistribuicao centroDistribuicao = null;
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
//        System.out.printf("Query: %s\n", sql);
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
//        System.out.printf("Query: %s\n", sql);
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
//        System.out.printf("Query: %s\n", sql);
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, entityId);
                ps.executeUpdate();
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
}
