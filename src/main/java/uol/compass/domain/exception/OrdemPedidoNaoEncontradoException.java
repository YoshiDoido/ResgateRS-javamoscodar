package uol.compass.domain.exception;

public class OrdemPedidoNaoEncontradoException extends BusinessException {

    public OrdemPedidoNaoEncontradoException(Integer id) {
        super(String.format("Ordem de Pedido de código %d não foi encontrada", id));
    }

    public OrdemPedidoNaoEncontradoException(Integer id, Throwable cause) {
        super(String.format("Centro de Distribuição de código %d não foi encontrado", id), cause);
    }
}
