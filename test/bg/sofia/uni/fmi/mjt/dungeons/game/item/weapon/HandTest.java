package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandTest {
    @Test
    void testLevel() {
        Hand hand = new Hand();
        assertEquals(0, hand.level(), "The level of the Hand weapon should be 0.");
    }

    @Test
    void testGetName() {
        Hand hand = new Hand();
        assertEquals("HAND", hand.getName(), "The name of the weapon should be 'HAND'.");
    }

    @Test
    void testAttack() {
        Hand hand = new Hand();
        assertEquals(15, hand.attack(), "The attack damage of the Hand weapon should be 15.");
    }
}
