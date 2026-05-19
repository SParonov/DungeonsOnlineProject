package bg.sofia.uni.fmi.mjt.dungeons.request;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestTest {
    @Test
    void testRequestWithValidData() {
        String command = "start_game";
        int playerID = 1;
        Request request = new Request(command, playerID);

        assertNotNull(request);
        assertEquals(command, request.command());
        assertEquals(playerID, request.playerID());
    }

    @Test
    void testRequestWithNullCommand() {
        int playerID = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Request(null, playerID));

        assertEquals("Command should not be null", exception.getMessage());
    }

    @Test
    void testRequestWithInvalidPlayerID() {
        String command = "start_game";
        int playerID = 0;

        Exception exception = assertThrows(InvalidPlayerIDException.class, () -> new Request(command, playerID));

        assertEquals("Player ID should be between 1 and 9", exception.getMessage());
    }

    @Test
    void testRequestWithValidPlayerID() {
        String command = "start_game";
        int playerID = 5;
        Request request = new Request(command, playerID);

        assertNotNull(request);
        assertEquals(command, request.command());
        assertEquals(playerID, request.playerID());
    }
}
