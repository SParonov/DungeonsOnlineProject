package bg.sofia.uni.fmi.mjt.dungeons.game.item.potion;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HealthPotionTest {
    @Test
    void testConsumeWithValidStats() {
        Stats mockStats = mock(Stats.class);
        when(mockStats.getMana()).thenReturn(100);
        when(mockStats.getHealth()).thenReturn(50);

        HealthPotion healthPotion = new HealthPotion();
        healthPotion.consume(mockStats);

        verify(mockStats).setMana(25);
        verify(mockStats).setHealth(100);
    }

    @Test
    void testConsumeWithNullStats() {
        HealthPotion healthPotion = new HealthPotion();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> healthPotion.consume(null));
        assertEquals("Stats should not be null", exception.getMessage());
    }

    @Test
    void testGetManaCost() {
        HealthPotion healthPotion = new HealthPotion();
        assertEquals(75, healthPotion.getManaCost());
    }

    @Test
    void testGetName() {
        HealthPotion healthPotion = new HealthPotion();
        assertEquals("HEALTH POTION", healthPotion.getName());
    }
}
