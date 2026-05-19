package bg.sofia.uni.fmi.mjt.dungeons.exceptions;

public class InvalidPortException extends RuntimeException {
    public InvalidPortException(String message) {
        super(message);
    }

    public InvalidPortException(String message, Throwable cause) {
        super(message, cause);
    }
}
