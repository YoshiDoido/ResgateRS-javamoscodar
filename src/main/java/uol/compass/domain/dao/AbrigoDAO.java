package uol.compass.domain.dao;

import uol.compass.domain.model.Abrigo;
import uol.compass.domain.model.dto.AbrigoNecessidades;
import uol.compass.domain.model.dto.CentroDistribuicaoAbrigoNecessidade;

import java.util.List;

public interface AbrigoDAO extends SimpleCrud<Abrigo, Integer> {
    List<CentroDistribuicaoAbrigoNecessidade> listarNecessidades(AbrigoNecessidades abrigoNecessidades);
    Integer getAbrigoAmazemId(Integer abrigoId);
}
