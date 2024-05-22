package uol.compass.domain.dao;

import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Doacao;

import java.util.List;
import java.util.Map;

public interface CentroDistribuicaoDAO extends SimpleCrud<CentroDistribuicao, Integer>{
    Doacao inserirDoacao(Integer id, Doacao doacao);
    List<Doacao> findAllDoacoes(Integer id);
    Map<Doacao.Categoria, Integer> totalDoacoes(Integer id);
    Integer getArmazemId(Integer id);
}
