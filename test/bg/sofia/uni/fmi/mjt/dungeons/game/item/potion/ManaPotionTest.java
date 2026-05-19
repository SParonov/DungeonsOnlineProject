package bg.sofia.uni.fmi.mjt.dungeons.game.item.potion;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ManaPotionTest {
    @Test
    void testConsumeWithValidStats() {
        Stats mockStats = mock(Stats.class);
        when(mockStats.getMana()).thenReturn(50);

        ManaPotion manaPotion = new ManaPotion();
        manaPotion.consume(mockStats);

        verify(mockStats).setMana(35);
        verify(mockStats).setMana(135);
    }

    @Test
    void testConsumeWithNullStats() {
        ManaPotion manaPotion = new ManaPotion();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> manaPotion.consume(null));
        assertEquals("Stats should not be null", exception.getMessage());
    }

    @Test
    void testGetManaCost() {
        ManaPotion manaPotion = new ManaPotion();
        assertEquals(15, manaPotion.getManaCost());
    }

    @Test
    void testGetName() {
        ManaPotion manaPotion = new ManaPotion();
        assertEquals("MANA POTION", manaPotion.getName());
    }
}
