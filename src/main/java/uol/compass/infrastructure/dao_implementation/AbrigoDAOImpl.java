package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.AbrigoDAO;
import uol.compass.domain.model.Abrigo;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.model.dto.AbrigoNecessidades;
import uol.compass.domain.model.dto.CentroDistribuicaoAbrigoNecessidade;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AbrigoDAOImpl implements AbrigoDAO {

    @Override
    public List<Abrigo> findAll() {
        String sql = "SELECT * FROM abrigos";
        List<Abrigo> abrigos = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    abrigos.add(createNewAbrigoObject(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todos abrigos", e);
        }

        return abrigos;
    }

    @Override
    public Optional<Abrigo> findById(Integer entityId) {
        String sql = "SELECT * FROM abrigos WHERE id = ?";
        Abrigo abrigo = null;
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entityId);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    abrigo = createNewAbrigoObject(rs);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar abrigo", e);
        }

        return Optional.ofNullable(abrigo);
    }

    private Abrigo createNewAbrigoObject(ResultSet rs) throws SQLException {
        var abrigo = new Abrigo();
        abrigo.setId(rs.getInt("id"));
        abrigo.setNome(rs.getString("nome"));
        abrigo.setEndereco(rs.getString("endereco"));
        abrigo.setCep(rs.getString("cep"));
        abrigo.setCidade(rs.getString("cidade"));
        abrigo.setResponsavel(rs.getString("responsavel"));
        abrigo.setTelefone(rs.getString("telefone"));
        abrigo.setEmail(rs.getString("email"));
        abrigo.setCapacidade(rs.getInt("capacidade"));
        abrigo.setOcupacao(rs.getInt("ocupacao"));

        return abrigo;
    }

    @Override
    public <S extends Abrigo> S save(S entity) {
        String sql = "INSERT INTO abrigos(nome, endereco, cep, cidade, responsavel, telefone, email, capacidade, ocupacao) " +
                "VALUES(?, ?, ?,?, ?, ?,?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                setPreparedStatementValues(entity, ps);
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
            throw new RepositoryException("Falha ao salvar abrigo", e);
        }

        return entity;
    }

    @Override
    public <S extends Abrigo> S update(Integer entityId, S entity) {
        String sql = "UPDATE abrigos SET nome = ?, endereco = ?, cep = ?, cidade = ?, responsavel = ?, telefone = ?," +
                " email = ?, capacidade = ?, ocupacao = ? WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                setPreparedStatementValues(entity, ps);
                ps.setInt(10, entityId);
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

    private <S extends Abrigo> void setPreparedStatementValues(S entity, PreparedStatement ps) throws SQLException {
        ps.setString(1, entity.getNome());
        ps.setString(2, entity.getEndereco());
        ps.setString(3, entity.getCep());
        ps.setString(4, entity.getCidade());
        ps.setString(5, entity.getResponsavel());
        ps.setString(6, entity.getTelefone());
        ps.setString(7, entity.getEmail());
        ps.setInt(8, entity.getCapacidade());
        ps.setInt(9, entity.getOcupacao());
    }

    @Override
    public void deleteById(Integer entityId) {
        String sql = "DELETE FROM abrigos WHERE id = ?";
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
            throw new RepositoryException("Falha ao apagar abrigo", e);
        }
    }

    @Override
    public List<CentroDistribuicaoAbrigoNecessidade> listarNecessidades(AbrigoNecessidades abrigoNecessidades) {
        List<CentroDistribuicaoAbrigoNecessidade> centros = new ArrayList<>();
        String sql = "SELECT cd.id AS centro_distribuicao_id, p.item, SUM(quantidade) as 'quantidade' FROM centro_distribuicao cd " +
                "INNER JOIN armazem a ON (a.centro_distribuicao_id = cd.id) " +
                "INNER JOIN produtos p ON (p.armazem_id = a.id) " +
                "WHERE p.item = ? GROUP BY cd.id";

        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, abrigoNecessidades.getItem().name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var centroDistribuicao = new CentroDistribuicaoAbrigoNecessidade(
                            rs.getInt("centro_distribuicao_id"),
                            Doacao.Item.valueOf(rs.getString("item")),
                            rs.getInt("quantidade")
                    );
                    centros.add(centroDistribuicao);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao processar necessidades!", e);
        }

        return centros;
    }
}
