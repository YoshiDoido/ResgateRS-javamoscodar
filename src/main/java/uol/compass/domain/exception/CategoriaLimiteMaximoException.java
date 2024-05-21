package uol.compass.domain.exception;

public class CategoriaLimiteMaximoException extends BusinessException {

    public CategoriaLimiteMaximoException(String message) {
        super(message);
    }

    public CategoriaLimiteMaximoException(String message, Throwable cause) {
        super(message, cause);
    }
}
