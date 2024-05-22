package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.ArmazemDAO;
import uol.compass.domain.model.dto.TipoArmazem;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArmazemDAOImpl implements ArmazemDAO {

    @Override
    public void save(Integer referenceId, TipoArmazem tipo) {
        String sql = "INSERT INTO armazem(centro_distribuicao_id) VALUES(?)";
        if (tipo.equals(TipoArmazem.ABRIGO)) {
            sql = "INSERT INTO armazem(abrigo_id) VALUES(?)";
        }
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, referenceId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao criar armazem", e);
        }
    }
}
