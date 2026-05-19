package bg.sofia.uni.fmi.mjt.dungeons.response;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapResponseTest {
    @Test
    void testMapResponseWithValidData() {
        List<List<String>> validMap = Arrays.asList(
            Arrays.asList("a", "b", "c"),
            Arrays.asList("d", "e", "f")
        );

        MapResponse response = new MapResponse("Success", validMap);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validMap, response.getMap());
    }

    @Test
    void testMapResponseWithNullMap() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MapResponse("Error", null));

        assertEquals("Map should not be null", exception.getMessage());
    }

    @Test
    void testMapResponseWithNullRow() {
        List<List<String>> invalidMap = Arrays.asList(
            Arrays.asList("a", "b"),
            null
        );

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MapResponse("Error", invalidMap));

        assertEquals("Row should not be null", exception.getMessage());
    }

    @Test
    void testMapResponseWithEmptyRow() {
        List<List<String>> invalidMap = Arrays.asList(
            Arrays.asList("a", "b"),
            List.of()
        );

        MapResponse response = new MapResponse("Success", invalidMap);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(invalidMap, response.getMap());
    }

    @Test
    void testMapResponseWithEmptyMap() {
        List<List<String>> emptyMap = List.of();

        MapResponse response = new MapResponse("Success", emptyMap);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(emptyMap, response.getMap());
    }

    @Test
    void testMapResponseWithNullStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MapResponse(null, List.of(Arrays.asList("a", "b"))));

        assertEquals("Status should not be null", exception.getMessage());
    }

    @Test
    void testMapResponseWithBlankStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new MapResponse(" ", List.of(Arrays.asList("a", "b"))));

        assertEquals("Status should not be blank", exception.getMessage());
    }
}
