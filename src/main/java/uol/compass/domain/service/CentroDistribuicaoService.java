package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.exception.CategoriaLimiteMaximoException;
import uol.compass.domain.exception.CentroDeDistribuicaoNaoEncontradoException;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Produto;
import uol.compass.domain.dao.CentroDistribuicaoDAO;
import uol.compass.infrastructure.dao_implementation.CentroDistribuicaoDAOImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CentroDistribuicaoService {

    public static final int CATEGORIA_ESTOQUE_MAXIMO = 1000;
    private final CentroDistribuicaoDAO centroDistribuicaoDAO;

    public CentroDistribuicaoService() {
        this.centroDistribuicaoDAO = new CentroDistribuicaoDAOImpl();;
    }

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
        System.out.println();
        log.info("Novo centro de distribuição de id {} salvo com sucesso!\n", savedCentroDistribuicao.getId());
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


    public Produto inserirDoacao(Integer id, Produto produto) {
        centroDistribuicaoDAO.totalDoacoes(id).forEach((categoria, quantidade) -> {
            if (produto.getCategoria().equals(categoria) && quantidade >= CATEGORIA_ESTOQUE_MAXIMO) {
                throw new CategoriaLimiteMaximoException(
                        String.format("Categoria %s atingiu seu limitie máximo (1000).", categoria)
                );
            }
            int total = quantidade + produto.getQuantidade();
            if (produto.getCategoria().equals(categoria) && total > CATEGORIA_ESTOQUE_MAXIMO) {
                produto.setQuantidade(produto.getQuantidade() - (total - CATEGORIA_ESTOQUE_MAXIMO));
                System.out.println("\nQuantidade ultrapassou o limite da categoria. Ajuste feito\n");
            }
        });
        Produto produtoSalvo = centroDistribuicaoDAO.inserirDoacao(id, produto);
        System.out.println();
        log.info("Novo produto de id {} salvo com sucesso!\n", produtoSalvo.getId());
        return produtoSalvo;
    }


    public List<Produto> listAllProdutosCentroDistribuicao(Integer id) {
        findByIdOrException(id);
        centroDistribuicaoTotalDoacoes(id);
        return centroDistribuicaoDAO.findAllDoacoes(id);
    }

    public void centroDistribuicaoTotalDoacoes(Integer id) {
        centroDistribuicaoDAO.totalDoacoes(id).forEach(
                (categoria, quantidade) -> System.out.println(categoria + ": " + quantidade)
        );
    }
}
