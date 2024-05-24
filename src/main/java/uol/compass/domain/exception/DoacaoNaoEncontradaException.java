package uol.compass.domain.exception;

public class DoacaoNaoEncontradaException extends BusinessException {

    public DoacaoNaoEncontradaException() {
        super("Não foi possivel encontrar doação");
    }

    public DoacaoNaoEncontradaException(Throwable cause) {
        super("Não foi possivel encontrar doação", cause);
    }
}
