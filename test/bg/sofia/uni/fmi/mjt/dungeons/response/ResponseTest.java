package bg.sofia.uni.fmi.mjt.dungeons.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResponseTest {
    private static final String VALID_STATUS = "OK";

    private Response response;

    @BeforeEach
    void setUp() {
        response = new Response(VALID_STATUS);
    }

    @Test
    void testConstructorWithValidStatus() {
        assertEquals(VALID_STATUS, response.getStatus(),
            "The constructor should properly assign the provided status");
    }

    @Test
    void testConstructorWithNullStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new Response(null));

        assertEquals("Status should not be null", exception.getMessage(),
            "Constructor should throw IllegalArgumentException when status is null");
    }

    @Test
    void testConstructorWithBlankStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new Response(" "));

        assertEquals("Status should not be blank", exception.getMessage(),
            "Constructor should throw IllegalArgumentException when status is blank");
    }

    @Test
    void testGetStatus() {
        assertEquals(VALID_STATUS, response.getStatus(),
            "getStatus() should return the correct status value");
    }
}
