package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.exception.CentroDeDistribuicaoNaoEncontradoException;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Produto;
import uol.compass.domain.dao.CentroDistribuicaoRepository;
import uol.compass.infrastructure.dao_implementation.CentroDistribuicaoRepositoryImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CentroDistribuicaoService {

    private final CentroDistribuicaoRepository centroDistribuicaoRepository;

    public CentroDistribuicaoService() {
        this.centroDistribuicaoRepository = new CentroDistribuicaoRepositoryImpl();;
    }

    public List<CentroDistribuicao> findAll() {
        return centroDistribuicaoRepository.findAll();
    }

    public CentroDistribuicao findByIdOrException(Integer id) {
        Objects.requireNonNull(id);
        return centroDistribuicaoRepository.findById(id)
                .orElseThrow(() -> new CentroDeDistribuicaoNaoEncontradoException(id));
    }

    public CentroDistribuicao save(CentroDistribuicao centroDistribuicao) {
        CentroDistribuicao savedCentroDistribuicao = centroDistribuicaoRepository.save(centroDistribuicao);
        System.out.println();
        log.info("Novo centro de distribuição de id {} salvo com sucesso!\n", savedCentroDistribuicao.getId());
        return savedCentroDistribuicao;
    }

    public CentroDistribuicao update(Integer id, CentroDistribuicao centroDistribuicao) {
        CentroDistribuicao update = centroDistribuicaoRepository.update(id, centroDistribuicao);
        System.out.println();
        log.info("Centro de distribuição de id {} atualizado com sucesso!", id);
        return update;
    }


    public void deleteById(Integer id) {
        centroDistribuicaoRepository.deleteById(id);
        System.out.println();
        log.info("Centro de distribuição de id {} apagado com sucesso!", id);
    }


    public Produto inserirDoacao(Integer id, Produto produto) {
        Produto produtoSalvo = centroDistribuicaoRepository.inserirDoacao(id, produto);
        System.out.println();
        log.info("Novo produto de id {} salvo com sucesso!\n", produtoSalvo.getId());
        return produtoSalvo;
    }

}
