package uol.compass.infrastructure.dao_implementation;

import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.domain.model.Doacao;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DoacaoDAOImpl implements DoacaoDAO {


    @Override
    public List<Doacao> findCentroDistribuicaoAllDoacoes(Integer id) {
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
    public void removerDoacao(Integer doacaoId) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, doacaoId);
                ps.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao apagar uma doação");
        }
    }

    @Override
    public Doacao atualizarDoacao(Integer id, Doacao doacao) {
        String sql = "UPDATE produtos SET quantidade = ? WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, doacao.getQuantidade());
                ps.setInt(2, doacao.getId());
                ps.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao apagar uma doação");
        }
        return doacao;
    }


    @Override
    public Doacao atualizarDoacaoDeItemIgual(Doacao doacao, Doacao doacaoAtual) {
        String sql = "UPDATE produtos p SET quantidade = ? where armazem_id = ? and p.item = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, doacaoAtual.getQuantidade() + doacao.getQuantidade());
                ps.setInt(2, doacao.getArmazemId());
                ps.setString(3, doacao.getItem().name());
                ps.executeUpdate();

                conn.commit();
                doacaoAtual.setQuantidade(doacaoAtual.getQuantidade() + doacao.getQuantidade());
                return doacaoAtual;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao atualizar produto do armazem", e);
        }
    }

    @Override
    public Optional<Doacao> getDoacaoByCentroIdAndItem(Integer centroId, Doacao.Item item) {
        Doacao produto = null;
        String sql = "SELECT p.* from centro_distribuicao cd " +
                "inner join armazem a on (a.centro_distribuicao_id = cd.id) " +
                "inner join produtos p on (p.armazem_id = a.id) " +
                "where cd.id = ? and p.item = ?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, centroId);
            ps.setString(2, item.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    produto = new Doacao();
                    produto.setId(rs.getInt("id"));
                    produto.setArmazemId(rs.getInt("armazem_id"));
                    produto.setCategoria(Doacao.Categoria.valueOf(rs.getString("categoria")));
                    produto.setItem(Doacao.Item.valueOf(rs.getString("item")));
                    if (produto.getCategoria().equals(Doacao.Categoria.ROUPA)) {
                        produto.setSexo(Doacao.Sexo.valueOf(rs.getString("sexo")));
                        produto.setTamanho(Doacao.Tamanho.valueOf(rs.getString("tamanho")));
                    }
                    produto.setQuantidade(rs.getInt("quantidade"));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar doação", e);
        }

        return Optional.ofNullable(produto);
    }

    @Override
    public Map<Doacao.Categoria, Integer> armazemTotalDoacoes(Integer id) {
        Map<Doacao.Categoria, Integer> categoriaQuantidade = new HashMap<>();
        String sql = "SELECT p.categoria, SUM(quantidade) AS 'quantidade' " +
                "FROM armazem a " +
                "INNER JOIN produtos p ON (p.armazem_id = a.id) " +
                "WHERE a.id = ? GROUP BY p.categoria";
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
            throw new RepositoryException("Falha ao recuperar todas doações do centro de distribuição", e);
        }

        return categoriaQuantidade;
    }

    @Override
    public int totalCategoria(Integer armazemId, Doacao.Categoria categoria) {
        int total = 0;
        String sql = "SELECT p.categoria, SUM(quantidade) AS 'quantidade' FROM armazem a " +
                "INNER JOIN produtos p ON (p.armazem_id = a.id) " +
                "WHERE a.id = ? AND p.categoria = ? " +
                "GROUP BY p.categoria;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, armazemId);
            ps.setString(2, categoria.name());
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total += rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar todas doações do centro de distribuição", e);
        }
        return total;
    }

    @Override
    public List<Doacao> getAllDoacoesAbrigo(Integer abrigoId) {
        List<Doacao> doacoes = new ArrayList<>();
        String sql = "SELECT p.* FROM abrigos ab " +
                "INNER JOIN armazem a ON (a.abrigo_id = ab.id) " +
                "INNER JOIN produtos p ON (p.armazem_id = a.id) " +
                "WHERE ab.id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, abrigoId);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var doacao = new Doacao();
                    doacao.setId(rs.getInt("id"));
                    doacao.setArmazemId(rs.getInt("armazem_id"));
                    doacao.setCategoria(Doacao.Categoria.valueOf(rs.getString("categoria")));
                    doacao.setItem(Doacao.Item.valueOf(rs.getString("item")));
                    if (doacao.getCategoria().equals(Doacao.Categoria.ROUPA)) {
                        doacao.setSexo(Doacao.Sexo.valueOf(rs.getString("sexo")));
                        doacao.setTamanho(Doacao.Tamanho.valueOf(rs.getString("tamanho")));
                    }
                    doacao.setQuantidade(rs.getInt("quantidade"));
                    doacoes.add(doacao);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao recuperar doações do abrigo", e);
        }
        return doacoes;
    }
}
