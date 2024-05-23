package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.OrdemPedidoDAO;
import uol.compass.domain.exception.OrdemPedidoNaoEncontradoException;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.infrastructure.dao_implementation.OrdemPedidoDAOImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
public class OrdemPedidoService {

   private final OrdemPedidoDAO ordemPedidoDAO = new OrdemPedidoDAOImpl();

    public List<OrdemPedido> findAll() {
        return ordemPedidoDAO.findAll();
    }

    public OrdemPedido findByIdOrException(Integer id) {
        Objects.requireNonNull(id);
        return ordemPedidoDAO.findById(id)
                .orElseThrow(() -> new OrdemPedidoNaoEncontradoException(id));
    }
}
