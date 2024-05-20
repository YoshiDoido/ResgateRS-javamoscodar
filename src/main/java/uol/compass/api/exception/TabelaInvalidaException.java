package uol.compass.api.exception;

public class TabelaInvalidaException extends RuntimeException {

    public TabelaInvalidaException(int id) {
        super(String.format("Tabela de id %d não existe", id));
    }

    public TabelaInvalidaException(int id, Throwable cause) {
        super(String.format("Tabela de id %d não existe", id), cause);
    }
}
