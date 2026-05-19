package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatsResponseTest {
    @Test
    void testStatsResponseWithValidData() {
        Stats stats = mock(Stats.class);
        when(stats.getHealth()).thenReturn(100);
        when(stats.getDefense()).thenReturn(50);

        StatsResponse response = new StatsResponse("Success", stats);

        assertNotNull(response, "StatsResponse should not be null");
        assertEquals("Success", response.getStatus(), "Status should be 'Success'");
        assertEquals(stats, response.getStats(), "Stats should be equal to the provided stats");
    }

    @Test
    void testStatsResponseWithNullStats() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new StatsResponse("Error", null));
        assertEquals("Stats should no be null", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testStatsResponseWithValidStats() {
        Stats stats = mock(Stats.class);
        when(stats.getHealth()).thenReturn(100);
        when(stats.getDefense()).thenReturn(50);

        StatsResponse response = new StatsResponse("Valid", stats);

        assertNotNull(response.getStats(), "Stats should not be null after valid response creation");
        assertEquals(stats, response.getStats(), "Stats should match the mocked stats");
    }

    @Test
    void testStatsResponseWithValidStatus() {
        Stats stats = mock(Stats.class);
        when(stats.getHealth()).thenReturn(100);
        when(stats.getDefense()).thenReturn(50);

        StatsResponse response = new StatsResponse("Success", stats);

        assertEquals("Success", response.getStatus(), "The status should be correctly assigned");
    }


    @Test
    void testStatsResponseWithBlankStatus() {
        Stats stats = mock(Stats.class);  // Mocking Stats class
        when(stats.getHealth()).thenReturn(100);
        when(stats.getDefense()).thenReturn(50);

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new StatsResponse(" ", stats));

        assertEquals("Status should not be blank", exception.getMessage(), "Blank status should throw an exception");
    }
}

