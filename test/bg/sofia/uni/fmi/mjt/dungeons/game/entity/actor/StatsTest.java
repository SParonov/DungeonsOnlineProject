package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatsTest {
    private Stats stats;

    @BeforeEach
    void setUp() {
        stats = new Stats(100, 50, 10, 5);
    }

    @Test
    void testConstructorAndInitialValues() {
        assertEquals(100, stats.getHealth(), "Health should be initialized to 100");
        assertEquals(50, stats.getMana(), "Mana should be initialized to 50");
        assertEquals(10, stats.getAttack(), "Attack should be initialized to 10");
        assertEquals(5, stats.getDefense(), "Defense should be initialized to 5");
    }

    @Test
    void testSetHealth() {
        stats.setHealth(80);
        assertEquals(80, stats.getHealth(), "Health should be set to 80");

        stats.setHealth(150);
        assertEquals(100, stats.getHealth(), "Health should not exceed maxHealth and should be set to maxHealth (100)");

        assertThrows(IllegalArgumentException.class, () -> stats.setHealth(-10), "Health should not be negative");
    }

    @Test
    void testSetMana() {
        stats.setMana(30);
        assertEquals(30, stats.getMana(), "Mana should be set to 30");

        stats.setMana(60);
        assertEquals(50, stats.getMana(), "Mana should not exceed maxMana and should be set to maxMana (50)");

        assertThrows(IllegalArgumentException.class, () -> stats.setMana(-10), "Mana should not be negative");
    }

    @Test
    void testLevelUp() {
        stats.levelUp();

        assertEquals(110, stats.getHealth(), "Health should increase by 10 after level up");
        assertEquals(60, stats.getMana(), "Mana should increase by 10 after level up");
        assertEquals(15, stats.getAttack(), "Attack should increase by 5 after level up");
        assertEquals(10, stats.getDefense(), "Defense should increase by 5 after level up");
    }

    @Test
    void testSetNegativeHealth() {
        assertThrows(IllegalArgumentException.class, () -> stats.setHealth(-10), "Health cannot be negative");
    }

    @Test
    void testSetNegativeMana() {
        assertThrows(IllegalArgumentException.class, () -> stats.setMana(-10), "Mana cannot be negative");
    }

    @Test
    void testPrintStats() {
        Stats stats = new Stats(100, 50, 10, 5);
        stats.printStats();
    }
}
