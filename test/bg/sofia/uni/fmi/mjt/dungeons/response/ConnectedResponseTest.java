package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectedResponseTest {
    @Test
    void testConnectedResponseWithValidData() {
        String validMessage = "Player connected";
        int validPlayerID = 5;
        ConnectedResponse response = new ConnectedResponse("Success", validMessage, validPlayerID);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validMessage, response.getMessage());
        assertEquals(validPlayerID, response.getPlayerID());
    }

    @Test
    void testConnectedResponseWithNullMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new ConnectedResponse("Error", null, 1));

        assertEquals("Message should not be null", exception.getMessage());
    }

    @Test
    void testConnectedResponseWithBlankMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new ConnectedResponse("Error", " ", 1));

        assertEquals("Message should not be blank", exception.getMessage());
    }

    @Test
    void testConnectedResponseWithInvalidPlayerID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> new ConnectedResponse("Error", "Player connected", 0));

        assertEquals("Player ID should be between 1 and 9", exception.getMessage());
    }

    @Test
    void testConnectedResponseWithValidPlayerID() {
        String validMessage = "Player connected";
        int validPlayerID = 1;
        ConnectedResponse response = new ConnectedResponse("Success", validMessage, validPlayerID);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validMessage, response.getMessage());
        assertEquals(validPlayerID, response.getPlayerID());
    }
}
