package bg.sofia.uni.fmi.mjt.dungeons.exceptions;

public class InvalidItemUseException extends RuntimeException {
    public InvalidItemUseException(String message) {
        super(message);
    }

    public InvalidItemUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
