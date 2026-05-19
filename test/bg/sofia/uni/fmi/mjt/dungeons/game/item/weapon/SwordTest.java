package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwordTest {
    @Test
    void testGetName() {
        Sword sword = new Sword(1);
        assertEquals("SWORD", sword.getName(), "The name of the weapon should be 'SWORD'.");
    }

    @Test
    void testAttackWithLevel1() {
        Sword sword = new Sword(1);
        assertEquals(20, sword.attack(), "The attack damage of the Sword with level 1 should be 20.");
    }

    @Test
    void testAttackWithLevel2() {
        Sword sword = new Sword(2);
        assertEquals(40, sword.attack(), "The attack damage of the Sword with level 2 should be 40.");
    }

    @Test
    void testAttackWithLevel3() {
        Sword sword = new Sword(3);
        assertEquals(60, sword.attack(), "The attack damage of the Sword with level 3 should be 60.");
    }
}
