// // package uol.compass.domain.service;

<<<<<<< HEAD
// // import lombok.extern.slf4j.Slf4j;
// // import uol.compass.domain.dao.OrdemPedidoDAO;
// // import uol.compass.domain.exception.OrdemPedidoNaoEncontradoException;
// // import uol.compass.domain.model.OrdemPedido;
=======
import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.OrdemPedidoDAO;
import uol.compass.domain.exception.OrdemPedidoNaoEncontradoException;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.infrastructure.dao_implementation.OrdemPedidoDAOImpl;
>>>>>>> 2e472296ab75f94537dd83d74a06dac67bf11232

// // import java.util.List;
// // import java.util.Objects;

// // @Slf4j
// // public class OrdemPedidoService {

<<<<<<< HEAD
// //     private final OrdemPedidoDAO ordemPedidoDAO = new OrdemPedidoDAOImpl();
=======
   private final OrdemPedidoDAO ordemPedidoDAO = new OrdemPedidoDAOImpl();
>>>>>>> 2e472296ab75f94537dd83d74a06dac67bf11232

// //     public List<OrdemPedido> findAll() {
// //         return ordemPedidoDAO.findAll();
// //     }

// //     public OrdemPedido findByIdOrException(Integer id) {
// //         Objects.requireNonNull(id);
// //         return ordemPedidoDAO.findById(id)
// //                 .orElseThrow(() -> new OrdemPedidoNaoEncontradoException(id));
// //     }
// // }
