package uol.compass.domain.dao;

import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Produto;

import java.util.List;
import java.util.Map;

public interface CentroDistribuicaoDAO extends SimpleCrud<CentroDistribuicao, Integer>{
    Produto inserirDoacao(Integer id, Produto produto);
    List<Produto> findAllDoacoes(Integer id);
    Map<Produto.Categoria, Integer> totalDoacoes(Integer id);
}
