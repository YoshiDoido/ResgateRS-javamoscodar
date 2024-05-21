package uol.compass.infrastructure.dao_implementation;
import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.domain.model.Doacao;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class DoacaoDAOImpl implements DoacaoDAO {


    @Override
    public List<Doacao> findAll() {
        return List.of();
    }

    @Override
    public Optional<Doacao> findById(Integer entityId) {
        return Optional.empty();
    }

    @Override
    public <S extends Doacao> S save(S entity) {
        String sql = "INSERT INTO doacao(centro, data) VALUES(?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getCentro());
                ps.setString(2, String.valueOf(entity.getData()));
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
    public <S extends Doacao> S update(Integer entityId, S entity) {
        return null;
    }

    @Override
    public void deleteById(Integer entityId) {

    }

}
