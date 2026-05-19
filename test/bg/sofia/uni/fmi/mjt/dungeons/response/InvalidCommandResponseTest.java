package bg.sofia.uni.fmi.mjt.dungeons.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvalidCommandResponseTest {
    @Test
    void testInvalidCommandResponseWithValidData() {
        String validMessage = "Invalid command executed";
        InvalidCommandResponse response = new InvalidCommandResponse("Error", validMessage);

        assertNotNull(response);
        assertEquals("Error", response.getStatus());
        assertEquals(validMessage, response.getMessage());
    }

    @Test
    void testInvalidCommandResponseWithNullMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new InvalidCommandResponse("Error", null));

        assertEquals("Message should not be null", exception.getMessage());
    }

    @Test
    void testInvalidCommandResponseWithBlankMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new InvalidCommandResponse("Error", " "));

        assertEquals("Message should not be blank", exception.getMessage());
    }

    @Test
    void testInvalidCommandResponseWithValidStatus() {
        String validMessage = "Invalid command executed";
        InvalidCommandResponse response = new InvalidCommandResponse("Success", validMessage);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validMessage, response.getMessage());
    }

    @Test
    void testInvalidCommandResponseWithNullStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new InvalidCommandResponse(null, "Invalid command"));

        assertEquals("Status should not be null", exception.getMessage());
    }

    @Test
    void testInvalidCommandResponseWithBlankStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new InvalidCommandResponse(" ", "Invalid command"));

        assertEquals("Status should not be blank", exception.getMessage());
    }
}
