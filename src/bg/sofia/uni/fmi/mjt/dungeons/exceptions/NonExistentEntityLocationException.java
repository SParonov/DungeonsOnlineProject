package bg.sofia.uni.fmi.mjt.dungeons.exceptions;

public class NonExistentEntityLocationException extends RuntimeException {
    public NonExistentEntityLocationException(String message) {
        super(message);
    }

    public NonExistentEntityLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
