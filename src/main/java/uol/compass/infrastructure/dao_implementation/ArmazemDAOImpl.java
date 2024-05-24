package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.ArmazemDAO;
import uol.compass.domain.dto.TipoArmazem;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArmazemDAOImpl implements ArmazemDAO {

    @Override
    public Integer save(Integer referenceId, TipoArmazem tipo) {
        String sql = "INSERT INTO armazem(centro_distribuicao_id) VALUES(?)";
        Integer armazemId = null;
        if (tipo.equals(TipoArmazem.ABRIGO)) {
            sql = "INSERT INTO armazem(abrigo_id) VALUES(?)";
        }
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, referenceId);
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        armazemId = rs.getInt(1);
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
            throw new RepositoryException("Falha ao criar armazem", e);
        }

        return armazemId;
    }
}
