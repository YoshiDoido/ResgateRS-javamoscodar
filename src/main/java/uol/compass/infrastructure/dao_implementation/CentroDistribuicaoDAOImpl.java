package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dto.OrdemPedidoHistorico;
import uol.compass.domain.dao.CentroDistribuicaoDAO;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.model.OrdemPedido;
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
    public Integer getCentroDistribuicaoArmazemId(Integer id) {
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

    @Override
    public List<OrdemPedido> getCentroDistribuicaoOrdensPedido(Integer id, OrdemPedidoHistorico status) {
        List<OrdemPedido> ordensPedido = new ArrayList<>();
        String sql = getSqlQueryOrdensPedido(status);
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var ordemPedido = new OrdemPedido();
                    ordemPedido.setId(rs.getInt("id"));
                    ordemPedido.setCentroDistribuicaoId(rs.getInt("centro_distribuicao_id"));
                    ordemPedido.setAbrigoId((Integer) rs.getObject("abrigo_id"));
                    ordemPedido.setCentroDistribuicaoEnvioId((Integer) rs.getObject("centro_distribuicao_envio"));
                    ordemPedido.setItem(Doacao.Item.valueOf(rs.getString("item")));
                    ordemPedido.setStatus(OrdemPedido.Status.valueOf(rs.getString("status")));
                    ordemPedido.setMotivo(rs.getString("motivo"));
                    ordemPedido.setQuantidade(rs.getInt("quantidade"));
                    ordemPedido.setCategoria(Doacao.Categoria.valueOf(rs.getString("categoria")));

                    ordensPedido.add(ordemPedido);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todas Ordens de Pedido do Centro de Distribuicao", e);
        }
        return ordensPedido;
    }


    private String getSqlQueryOrdensPedido(OrdemPedidoHistorico ordemPedidoHistorico) {
        String sql = "SELECT op.* FROM centro_distribuicao cd INNER JOIN ordem_pedidos op " +
                "ON (cd.id = op.centro_distribuicao_id) WHERE (cd.id = ? OR op.centro_distribuicao_envio = ?) " +
                "AND op.status = 'PENDENTE'";
        if (ordemPedidoHistorico.equals(OrdemPedidoHistorico.HISTORICO)) {
            sql = "SELECT op.* FROM centro_distribuicao cd INNER JOIN ordem_pedidos op " +
                    "ON (cd.id = op.centro_distribuicao_id) WHERE (cd.id = ? OR op.centro_distribuicao_envio = ?) " +
                    "AND op.status != 'PENDENTE'";
        }
        return sql;
    }
}
