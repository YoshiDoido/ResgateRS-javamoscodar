package uol.compass.api.exception;

public class OperacaoInvalidaException extends RuntimeException {

    public OperacaoInvalidaException(int id) {
        super(String.format("Operação de id %d não existe", id));
    }

    public OperacaoInvalidaException(int id, Throwable cause) {
        super(String.format("Operação de id %d não existe", id), cause);
    }
}
