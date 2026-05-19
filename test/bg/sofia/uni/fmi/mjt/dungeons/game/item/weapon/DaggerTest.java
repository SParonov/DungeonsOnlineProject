package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DaggerTest {
    @Test
    void testGetName() {
        Dagger dagger = new Dagger(1);
        assertEquals("DAGGER", dagger.getName(), "The name of the weapon should be 'DAGGER'.");
    }

    @Test
    void testAttackWithLevel1() {
        Dagger dagger = new Dagger(1);
        assertEquals(15, dagger.attack(), "The attack damage of the Dagger with level 1 should be 15.");
    }

    @Test
    void testAttackWithLevel2() {
        Dagger dagger = new Dagger(2);
        assertEquals(30, dagger.attack(), "The attack damage of the Dagger with level 2 should be 30.");
    }

    @Test
    void testAttackWithLevel3() {
        Dagger dagger = new Dagger(3);
        assertEquals(45, dagger.attack(), "The attack damage of the Dagger with level 3 should be 45.");
    }
}
