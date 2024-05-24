package uol.compass.domain.exception;

public class OrdemPedidoNaoEncontradaException extends BusinessException {

    public OrdemPedidoNaoEncontradaException(Integer id) {
        super(String.format("Ordem Pedido de código %d não foi encontrada", id));
    }

    public OrdemPedidoNaoEncontradaException(Integer id, Throwable cause) {
        super(String.format("Ordem Pedido de código %d não foi encontrada", id), cause);
    }
}
