package bg.sofia.uni.fmi.mjt.dungeons.exceptions;

public class InvalidPlayerIDException extends RuntimeException {
    public InvalidPlayerIDException(String message) {
        super(message);
    }

    public InvalidPlayerIDException(String message, Throwable cause) {
        super(message, cause);
    }
}
