package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.OrdemPedidoDAO;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdemPedidoDAOImpl implements OrdemPedidoDAO {

    @Override
    public List<OrdemPedido> findAll() {
        String sql = "SELECT * FROM ordem_pedidos";
        List<OrdemPedido> ordemPedidos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordemPedidos.add(createNewOrdemPedidoObject(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todos os pedidos", e);
        }
        return ordemPedidos;
    }

    @Override
    public Optional<OrdemPedido> findById(Integer entityId) {
        String sql = "SELECT * FROM ordem_pedidos WHERE id = ?";
        OrdemPedido ordemPedido = null;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ordemPedido = createNewOrdemPedidoObject(rs);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar pedido", e);
        }
        return Optional.ofNullable(ordemPedido);
    }

    private OrdemPedido createNewOrdemPedidoObject(ResultSet rs) throws SQLException {
        var ordemPedido = new OrdemPedido();
        ordemPedido.setId(rs.getInt("id"));
        ordemPedido.setCentroDistribuicaoId(rs.getInt("centroDistribuicaoId"));
        ordemPedido.setAbrigoId(rs.getInt("abrigoId"));
        ordemPedido.setItem(Doacao.Item.valueOf(rs.getString("item")));
        ordemPedido.setStatus(OrdemPedido.Status.valueOf(rs.getString("status")));
        ordemPedido.setQuantidade(rs.getInt("quantidade"));
        ordemPedido.setMotivo(rs.getString("motivo"));
        return ordemPedido;
    }

    @Override
    public <S extends OrdemPedido> S save(S entity) {
        String sql = "INSERT INTO ordem_pedidos(centroDistribuicaoId, abrigoId, item, status, quantidade, motivo) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                setPreparedStatementValues(entity, ps);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
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
            throw new RepositoryException("Falha ao salvar pedido", e);
        }
        return entity;
    }

    @Override
    public <S extends OrdemPedido> S update(Integer entityId, S entity) {
        String sql = "UPDATE ordem_pedidos SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                setPreparedStatementValues(entity, ps);
                ps.setInt(5, entityId);
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
            throw new RepositoryException("Falha ao atualizar pedido", e);
        }

        return entity;
    }

    private <S extends OrdemPedido> void setPreparedStatementValues(S entity, PreparedStatement ps) throws SQLException {
        ps.setInt(1, entity.getCentroDistribuicaoId());
        ps.setString(2, entity.getItem().toString());
        ps.setInt(3, entity.getQuantidade());
    }

    @Override
    public void deleteById(Integer entityId) {
        String sql = "DELETE FROM ordem_pedidos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
            throw new RepositoryException("Falha ao apagar pedido", e);
        }
    }

}