package bg.sofia.uni.fmi.mjt.dungeons.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageResponseTest {
    @Test
    void testMessageResponseWithValidData() {
        MessageResponse response = new MessageResponse("Success", "Valid message");

        assertNotNull(response, "MessageResponse should not be null");
        assertEquals("Success", response.getStatus(), "Status should be 'Success'");
        assertEquals("Valid message", response.getMessage(), "Message should be 'Valid message'");
    }

    @Test
    void testMessageResponseWithNullMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MessageResponse("Error", null));

        assertEquals("Message should not be null", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testMessageResponseWithBlankMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MessageResponse("Error", " "));

        assertEquals("Message should not be blank", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testMessageResponseWithEmptyMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MessageResponse("Error", ""));

        assertEquals("Message should not be blank", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testMessageResponseWithValidStatus() {
        MessageResponse response = new MessageResponse("Success", "This is a valid message");

        assertEquals("Success", response.getStatus(), "Status should be correctly assigned");
    }

    @Test
    void testMessageResponseWithBlankStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MessageResponse(" ", "Valid message"));

        assertEquals("Status should not be blank", exception.getMessage(), "Blank status should throw an exception");
    }
}

