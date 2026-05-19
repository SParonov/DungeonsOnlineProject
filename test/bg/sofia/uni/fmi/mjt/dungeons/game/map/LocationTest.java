package bg.sofia.uni.fmi.mjt.dungeons.game.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationTest {
    @Test
    void testValidLocation() {
        Location location = new Location(5, 10);

        assertEquals(5, location.i(), "The i coordinate should be 5.");
        assertEquals(10, location.j(), "The j coordinate should be 10.");
    }

    @Test
    void testNegativeICoordinate() {
        assertThrows(IllegalArgumentException.class, () -> new Location(-1, 10), "i should not be negative");
    }

    @Test
    void testNegativeJCoordinate() {
        assertThrows(IllegalArgumentException.class, () -> new Location(5, -10), "j should not be negative");
    }

    @Test
    void testNegativeCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Location(-1, -10), "i should not be negative");
    }

    @Test
    void testZeroCoordinates() {
        Location location = new Location(0, 0);

        assertEquals(0, location.i(), "The i coordinate should be 0.");
        assertEquals(0, location.j(), "The j coordinate should be 0.");
    }
}
