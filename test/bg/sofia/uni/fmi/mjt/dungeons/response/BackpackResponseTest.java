package bg.sofia.uni.fmi.mjt.dungeons.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BackpackResponseTest {
    @Test
    void testBackpackResponseWithValidData() {
        String validBackpackContent = "Sword, Shield, Potion";
        BackpackResponse response = new BackpackResponse("Success", validBackpackContent);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validBackpackContent, response.getBackpackContent());
    }

    @Test
    void testBackpackResponseWithNullContent() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new BackpackResponse("Error", null));

        assertEquals("Backpack content should not be null", exception.getMessage());
    }

    @Test
    void testBackpackResponseWithBlankContent() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new BackpackResponse("Error", " "));

        assertEquals("Backpack content should not be blank", exception.getMessage());
    }

    @Test
    void testBackpackResponseWithValidContent() {
        String validBackpackContent = "Axe, Healing Potion";
        BackpackResponse response = new BackpackResponse("Success", validBackpackContent);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validBackpackContent, response.getBackpackContent());
    }
}
