package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.ArmazemDAO;
import uol.compass.domain.exception.CategoriaLimiteMaximoException;
import uol.compass.domain.exception.CentroDeDistribuicaoNaoEncontradoException;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.dao.CentroDistribuicaoDAO;
import uol.compass.domain.model.dto.TipoArmazem;
import uol.compass.infrastructure.dao_implementation.ArmazemDAOImpl;
import uol.compass.infrastructure.dao_implementation.CentroDistribuicaoDAOImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CentroDistribuicaoService {

    public static final int CATEGORIA_ESTOQUE_MAXIMO = 1000;
    private final CentroDistribuicaoDAO centroDistribuicaoDAO = new CentroDistribuicaoDAOImpl();
    private final ArmazemDAO armazemDAO = new ArmazemDAOImpl();

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
        centroDistribuicaoDAO.totalDoacoes(id).forEach((categoria, quantidade) -> {
            if (doacao.getCategoria().equals(categoria) && quantidade >= CATEGORIA_ESTOQUE_MAXIMO) {
                throw new CategoriaLimiteMaximoException(
                        String.format("Categoria %s atingiu seu limitie máximo (1000).", categoria)
                );
            }
            int total = quantidade + doacao.getQuantidade();
            if (doacao.getCategoria().equals(categoria) && total > CATEGORIA_ESTOQUE_MAXIMO) {
                doacao.setQuantidade(doacao.getQuantidade() - (total - CATEGORIA_ESTOQUE_MAXIMO));
                System.out.println("\nQuantidade ultrapassou o limite da categoria. Ajuste feito\n");
            }
        });
        Doacao doacaoSalvo = centroDistribuicaoDAO.inserirDoacao(id, doacao);
        System.out.println();
        log.info("Novo produto de id {} salvo com sucesso!\n", doacaoSalvo.getId());
        return doacaoSalvo;
    }


    public List<Doacao> listAllProdutosCentroDistribuicao(Integer id) {
        System.out.println();
        findByIdOrException(id);
        centroDistribuicaoTotalDoacoes(id);
        return centroDistribuicaoDAO.findAllDoacoes(id);
    }

    public void centroDistribuicaoTotalDoacoes(Integer id) {
        centroDistribuicaoDAO.totalDoacoes(id).forEach(
                (categoria, quantidade) -> System.out.println(categoria + ": " + quantidade)
        );
    }

    public int getCentroDistribuicaoArmazemId(Integer id) {
        return centroDistribuicaoDAO.getArmazemId(id);
    }
}
