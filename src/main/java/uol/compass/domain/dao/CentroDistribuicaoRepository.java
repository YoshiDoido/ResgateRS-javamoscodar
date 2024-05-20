package uol.compass.domain.dao;

import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Produto;

public interface CentroDistribuicaoRepository extends SimpleCrud<CentroDistribuicao, Integer>{
    Produto inserirDoacao(Integer id, Produto produto);
}
