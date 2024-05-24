package uol.compass.domain.dao;

import uol.compass.domain.dto.OrdemPedidoHistorico;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.OrdemPedido;

import java.util.List;

public interface CentroDistribuicaoDAO extends SimpleCrud<CentroDistribuicao, Integer>{
    Integer getCentroDistribuicaoArmazemId(Integer id);
    List<OrdemPedido> getCentroDistribuicaoOrdensPedido(Integer id, OrdemPedidoHistorico status);
}
