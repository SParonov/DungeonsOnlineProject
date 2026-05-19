package bg.sofia.uni.fmi.mjt.dungeons.exceptions;

public class InvalidContainerAccessException extends RuntimeException {
    public InvalidContainerAccessException(String message) {
        super(message);
    }

    public InvalidContainerAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
