package bg.sofia.uni.fmi.mjt.dungeons.game.entity.other;

import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Sword;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreasureTest {
    private Potion mockPotion;
    private Weapon mockWeapon;

    @BeforeEach
    void setUp() {
        mockPotion = Mockito.mock(Potion.class);
        mockWeapon = Mockito.mock(Weapon.class);
    }

    @Test
    void testTreasureWithPotionAndWeapon() {
        Treasure treasure = new Treasure(mockPotion, mockWeapon);

        assertTrue(treasure.hasPotion(), "Treasure should have a potion.");
        assertTrue(treasure.hasWeapon(), "Treasure should have a weapon.");
        assertEquals(mockPotion, treasure.getRandomPotion(), "Potion should match the mockPotion.");
        assertEquals(mockWeapon, treasure.getRandomWeapon(), "Weapon should match the mockWeapon.");
    }

    @Test
    void testTreasureWithPotionOnly() {
        Treasure treasure = new Treasure(mockPotion);

        assertTrue(treasure.hasPotion(), "Treasure should have a potion.");
        assertFalse(treasure.hasWeapon(), "Treasure should not have a weapon.");
        assertEquals(mockPotion, treasure.getRandomPotion(), "Potion should match the mockPotion.");
    }

    @Test
    void testTreasureWithWeaponOnly() {
        Treasure treasure = new Treasure(mockWeapon);

        assertFalse(treasure.hasPotion(), "Treasure should not have a potion.");
        assertTrue(treasure.hasWeapon(), "Treasure should have a weapon.");
        assertEquals(mockWeapon, treasure.getRandomWeapon(), "Weapon should match the mockWeapon.");
    }

    @Test
    void testGenerateRandomTreasureWithPotion() {
        Treasure treasure = Treasure.generateRandomTreasure();
        assertTrue(treasure.hasPotion() || treasure.hasWeapon(), "Treasure should have either a potion or a weapon.");
    }

    @Test
    void testGenerateRandomTreasureWithWeapon() {
        Treasure treasure = Treasure.generateRandomTreasure();
        assertTrue(treasure.hasPotion() || treasure.hasWeapon(), "Treasure should have either a potion or a weapon.");
    }

    @Test
    void testGenerateRandomTreasureWithBothPotionAndWeapon() {
        Random mockRandom = Mockito.mock(Random.class);
        Mockito.when(mockRandom.nextBoolean()).thenReturn(true);

        Treasure treasure = Treasure.generateRandomTreasure();
        assertTrue(treasure.hasPotion() && treasure.hasWeapon(), "Treasure should have both a potion and a weapon.");
    }

}
