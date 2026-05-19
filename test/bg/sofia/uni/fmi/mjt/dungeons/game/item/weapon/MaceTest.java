package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaceTest {
    @Test
    void testGetName() {
        Mace mace = new Mace(1);
        assertEquals("MACE", mace.getName(), "The name of the weapon should be 'MACE'.");
    }

    @Test
    void testAttackWithLevel1() {
        Mace mace = new Mace(1);
        assertEquals(25, mace.attack(), "The attack damage of the Mace with level 1 should be 25.");
    }

    @Test
    void testAttackWithLevel2() {
        Mace mace = new Mace(2);
        assertEquals(50, mace.attack(), "The attack damage of the Mace with level 2 should be 50.");
    }

    @Test
    void testAttackWithLevel3() {
        Mace mace = new Mace(3);
        assertEquals(75, mace.attack(), "The attack damage of the Mace with level 3 should be 75.");
    }
}
