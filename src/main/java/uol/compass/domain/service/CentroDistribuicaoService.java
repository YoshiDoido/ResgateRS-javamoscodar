package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.ArmazemDAO;
import uol.compass.domain.dao.CentroDistribuicaoDAO;
import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.domain.dto.OrdemPedidoHistorico;
import uol.compass.domain.dto.TipoArmazem;
import uol.compass.domain.exception.CategoriaLimiteMaximoException;
import uol.compass.domain.exception.CentroDeDistribuicaoNaoEncontradoException;
import uol.compass.domain.exception.DoacaoNaoEncontradaException;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.infrastructure.dao_implementation.ArmazemDAOImpl;
import uol.compass.infrastructure.dao_implementation.CentroDistribuicaoDAOImpl;
import uol.compass.infrastructure.dao_implementation.DoacaoDAOImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CentroDistribuicaoService {

    public static final int CATEGORIA_CENTRO_ESTOQUE_MAXIMO = 1000;

    private final CentroDistribuicaoDAO centroDistribuicaoDAO = new CentroDistribuicaoDAOImpl();
    private final ArmazemDAO armazemDAO = new ArmazemDAOImpl();
    private final DoacaoDAO doacaoDAO = new DoacaoDAOImpl();

    public List<CentroDistribuicao> findAll() {
        return centroDistribuicaoDAO.findAll();
    }

    public CentroDistribuicao findByIdOrException(Integer id) {
        Objects.requireNonNull(id);
        return centroDistribuicaoDAO.findById(id)
                .orElseThrow(() -> new CentroDeDistribuicaoNaoEncontradoException(id));
    }

    public CentroDistribuicao save(CentroDistribuicao centroDistribuicao) {
        CentroDistribuicao savedCentroDistribuicao = centroDistribuicaoDAO.save(centroDistribuicao);
        armazemDAO.save(savedCentroDistribuicao.getId(), TipoArmazem.CENTRO_DISTRIBUICAO);
        System.out.println();
        log.info("Novo centro de distribuição de id {} salvo com sucesso!", savedCentroDistribuicao.getId());
        log.info("Armazem do centro de distribuição salvo com sucesso!\n");
        return savedCentroDistribuicao;
    }

    public CentroDistribuicao update(Integer id, CentroDistribuicao centroDistribuicao) {
        CentroDistribuicao update = centroDistribuicaoDAO.update(id, centroDistribuicao);
        System.out.println();
        log.info("Centro de distribuição de id {} atualizado com sucesso!", id);
        return update;
    }


    public void deleteById(Integer id) {
        centroDistribuicaoDAO.deleteById(id);
        System.out.println();
        log.info("Centro de distribuição de id {} apagado com sucesso!", id);
    }


    public Doacao inserirDoacao(Integer id, Doacao doacao) {
        var armazemId = centroDistribuicaoDAO.getCentroDistribuicaoArmazemId(id);
        int totalCategoria = doacaoDAO.totalCategoria(armazemId, doacao.getCategoria());
        int total = totalCategoria + doacao.getQuantidade();
        
        if (total > CATEGORIA_CENTRO_ESTOQUE_MAXIMO) {
            ajusteDoacaoQuantidade(doacao, total, doacao.getCategoria());
        }

        Doacao docaoDeMesmoTipoJaExisteNoArmazem = null;
        var listaProdutosCentroArmazem = listAllProdutosCentroDistribuicao(id);
        for (Doacao doacaoPresenteNoArmazem : listaProdutosCentroArmazem) {
            if (doacaoPresenteNoArmazem.getItem().equals(doacao.getItem())) {
                docaoDeMesmoTipoJaExisteNoArmazem = doacaoPresenteNoArmazem;
            }
        }

        if (docaoDeMesmoTipoJaExisteNoArmazem != null) {
            var updatedDoacao = doacaoDAO.atualizarDoacaoDeItemIgual(doacao, docaoDeMesmoTipoJaExisteNoArmazem);
            System.out.println();
            log.info("Produto de id {} atualizado com sucesso!\n", updatedDoacao.getId());
            return updatedDoacao;
        }
        Doacao doacaoSalvo = doacaoDAO.inserirDoacao(id, doacao);
        log.info("Novo produto de id {} salvo com sucesso!\n", doacaoSalvo.getId());
        return doacaoSalvo;
    }

    private void ajusteDoacaoQuantidade(Doacao doacao, int total, Doacao.Categoria doacao1) {
        int novoValorDoacao = doacao.getQuantidade() - (total - CATEGORIA_CENTRO_ESTOQUE_MAXIMO);
        int valorAnterorDoacao = doacao.getQuantidade();
        doacao.setQuantidade(novoValorDoacao);
        System.out.printf("\nCategoria %s do Centro de Distribuição atingiu seu limite máximo." +
                        " Alterações na quantidade da doação foram feitas. " +
                        "\nAlterações: Quantidade da doação anterior %d | Quantidade da doação atual: %d%n",
                doacao1, valorAnterorDoacao, novoValorDoacao);
    }


    public List<Doacao> listAllProdutosCentroDistribuicao(Integer id) {
        findByIdOrException(id);
        return doacaoDAO.findCentroDistribuicaoAllDoacoes(id);
    }

    public Doacao getDoacaoByCentroDistribuicaoIdAndItem(Integer centroId, Doacao.Item item) {
        return doacaoDAO.getDoacaoByCentroIdAndItem(centroId, item)
                .orElseThrow(DoacaoNaoEncontradaException::new);
    }

    public void centroDistribuicaoTotalDoacoes(Integer id) {
        var armazemId = centroDistribuicaoDAO.getCentroDistribuicaoArmazemId(id);
        var totalMap = doacaoDAO.armazemTotalDoacoes(armazemId);
        Arrays.stream(Doacao.Categoria.values()).forEach(categoria -> {
            if (!totalMap.containsKey(categoria)) {
                totalMap.put(categoria, 0);
            }
        });
        totalMap.forEach((categoria, quantidade) -> System.out.print(categoria + ": " + quantidade + " | "));
        System.out.println();
    }

    public int getCentroDistribuicaoArmazemId(Integer id) {
        Integer centroDistribuicaoArmazemId = centroDistribuicaoDAO.getCentroDistribuicaoArmazemId(id);
        if (centroDistribuicaoArmazemId == null) {
            centroDistribuicaoArmazemId = armazemDAO.save(id, TipoArmazem.CENTRO_DISTRIBUICAO);
        }
        return centroDistribuicaoArmazemId;
    }


    public List<OrdemPedido> getCentroDistribuicaoOrdensPedido(Integer id, OrdemPedidoHistorico status) {
        return centroDistribuicaoDAO.getCentroDistribuicaoOrdensPedido(id, status);
    }


    public void verificarTotalCategoriaCentroDistribuicao(Integer centroId, Doacao.Categoria categoria) {
        int armazemId = getCentroDistribuicaoArmazemId(centroId);
        int totalCategoria = doacaoDAO.totalCategoria(armazemId, categoria);
        if (totalCategoria >= CATEGORIA_CENTRO_ESTOQUE_MAXIMO) {
            throw new CategoriaLimiteMaximoException(
                    String.format("Categoria %s atingiu o seu limite máximo (%d).", categoria,
                            CATEGORIA_CENTRO_ESTOQUE_MAXIMO)
            );
        }
    }

    public void removerDoacao(Integer doacaoId) {
        doacaoDAO.removerDoacao(doacaoId);
        log.info("Doação de id {} apagada com sucesso!", doacaoId);
    }
}
