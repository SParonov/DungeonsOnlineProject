package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinionTest {
    private Minion minion;

    @BeforeEach
    void setUp() {
        minion = Minion.generateRandomLevelMinion();
    }

    @Test
    void testMinionInitialization() {
        assertNotNull(minion.getStats(), "Minion's stats should be initialized.");
        assertTrue(minion.getLevel() >= 1 && minion.getLevel() <= Actor.MAX_LEVEL, "Minion's level should be between 1 and " + Actor.MAX_LEVEL);
    }

    @Test
    void testAttack() {
        int attack = minion.attack();
        assertEquals(minion.getStats().getAttack(), attack, "Minion's attack value should match its stats.");
    }

    @Test
    void testReceiveAttack() {
        int initialHealth = minion.getStats().getHealth();
        int attackDamage = 20;

        boolean isDead = minion.receiveAttack(attackDamage);

        assertFalse(isDead, "Minion should not be dead after receiving a small attack.");
        assertEquals(initialHealth - Math.max(attackDamage - minion.getStats().getDefense(), 1), minion.getStats().getHealth(), "Minion's health should decrease by the correct amount.");

        isDead = minion.receiveAttack(10000); // A large attack that should cause the minion to die
        assertTrue(isDead, "Minion should be dead after receiving a massive attack.");
    }

    @Test
    void testReceiveAttackWithInvalidDamage() {
        assertThrows(IllegalArgumentException.class, () -> minion.receiveAttack(-10), "Attack damage cannot be negative.");
    }
}
