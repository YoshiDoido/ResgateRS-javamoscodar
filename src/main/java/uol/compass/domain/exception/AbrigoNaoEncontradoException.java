package uol.compass.domain.exception;

public class AbrigoNaoEncontradoException extends BusinessException {

    public AbrigoNaoEncontradoException(Integer id) {
        super(String.format("Centro de Distribuição de código %d não foi encontrado", id));
    }

    public AbrigoNaoEncontradoException(Integer id, Throwable cause) {
        super(String.format("Centro de Distribuição de código %d não foi encontrado", id), cause);
    }
}
