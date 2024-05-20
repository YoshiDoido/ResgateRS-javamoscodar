package uol.compass.domain.exception;

public class CentroDeDistribuicaoNaoEncontradoException extends BusinessException {

    public CentroDeDistribuicaoNaoEncontradoException(Integer id) {
        super(String.format("Centro de Distribuição de código %d não foi encontrado", id));
    }

    public CentroDeDistribuicaoNaoEncontradoException(Integer id, Throwable cause) {
        super(String.format("Centro de Distribuição de código %d não foi encontrado", id), cause);
    }
}
