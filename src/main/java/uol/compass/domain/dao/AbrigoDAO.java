package uol.compass.domain.dao;

import uol.compass.domain.model.Abrigo;
import uol.compass.domain.dto.AbrigoNecessidades;
import uol.compass.domain.dto.CentroDistribuicaoAbrigoNecessidade;
import uol.compass.domain.model.OrdemPedido;

import java.util.List;

public interface AbrigoDAO extends SimpleCrud<Abrigo, Integer> {
    List<CentroDistribuicaoAbrigoNecessidade> listarNecessidades(AbrigoNecessidades abrigoNecessidades);
    List<OrdemPedido> abrigoOrdensPedido(Integer id);

    Integer getAbrigoArmazemId(Integer id);
}
