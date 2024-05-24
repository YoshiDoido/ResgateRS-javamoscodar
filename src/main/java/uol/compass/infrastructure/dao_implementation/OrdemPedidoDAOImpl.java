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
        List<OrdemPedido> ordemPedidoList = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var ordemPedido = new OrdemPedido();
                    setOrdemPedidoValues(ordemPedido, rs);
                    ordemPedidoList.add(ordemPedido);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todas Ordens de Pedido", e);
        }

        return ordemPedidoList;
    }

    @Override
    public Optional<OrdemPedido> findById(Integer entityId) {
        OrdemPedido ordemPedido = null;
        String sql = "SELECT * FROM ordem_pedidos WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entityId);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ordemPedido = new OrdemPedido();
                    setOrdemPedidoValues(ordemPedido, rs);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar Ordem de Pedido",e);
        }

        return Optional.ofNullable(ordemPedido);
    }

    private void setOrdemPedidoValues(OrdemPedido ordemPedido, ResultSet rs) throws SQLException {
        ordemPedido.setId(rs.getInt("id"));
        ordemPedido.setCentroDistribuicaoId(rs.getInt("centro_distribuicao_id"));
        ordemPedido.setAbrigoId(rs.getInt("abrigo_id"));
        ordemPedido.setItem(Doacao.Item.valueOf(rs.getString("item")));
        ordemPedido.setStatus(OrdemPedido.Status.valueOf(rs.getString("status")));
        ordemPedido.setQuantidade(rs.getInt("quantidade"));
        ordemPedido.setCategoria(Doacao.Categoria.valueOf(rs.getString("categoria")));
    }

    @Override
    public <S extends OrdemPedido> S save(S entity) {
        String sql = "INSERT INTO ordem_pedidos(centro_distribuicao_id, abrigo_id, item, STATUS, quantidade, categoria) " +
                "VALUES(?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, entity.getCentroDistribuicaoId());
                ps.setInt(2, entity.getAbrigoId());
                ps.setString(3, entity.getItem().name());
                ps.setString(4, "PENDENTE");
                ps.setInt(5, entity.getQuantidade());
                ps.setString(6, entity.getCategoria().name());
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                        entity.setStatus(OrdemPedido.Status.PENDENTE);
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
            throw new RepositoryException("Falha ao salvar nova Ordem de Pedido", e);
        }
        return entity;
    }

    @Override
    public <S extends OrdemPedido> S update(Integer entityId, S entity) {
        String sql = "UPDATE ordem_pedidos SET status = ? where id = ?";
        if (entity.getStatus().equals(OrdemPedido.Status.RECUSADO)) {
            sql = "UPDATE ordem_pedidos SET status = ?, motivo = ? where id = ?";
        }
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, entity.getStatus().name());
                ps.setInt(2, entityId);
                if (entity.getStatus().equals(OrdemPedido.Status.RECUSADO)) {
                    ps.setString(2, entity.getMotivo());
                    ps.setInt(3, entityId);
                }
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
            throw new RepositoryException("Falha ao atualizar Ordem de Pedido", e);
        }
        return entity;
    }

    @Override
    public void deleteById(Integer entityId) {
        String sql = "DELETE FROM ordem_pedidos WHERE id = ?";
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
            throw new RepositoryException("Falha ao apagar Ordem de Pedido", e);
        }
    }

}
