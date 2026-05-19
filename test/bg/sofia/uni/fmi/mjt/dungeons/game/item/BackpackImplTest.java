package bg.sofia.uni.fmi.mjt.dungeons.game.item;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidItemUseException;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BackpackImplTest {
    private BackpackImpl backpack;
    private Potion mockPotion;
    private Weapon mockWeapon;

    @BeforeEach
    void setUp() {
        backpack = new BackpackImpl();
        mockPotion = Mockito.mock(Potion.class);
        mockWeapon = Mockito.mock(Weapon.class);
        Mockito.when(mockPotion.getName()).thenReturn("Health Potion");
        Mockito.when(mockWeapon.getName()).thenReturn("Sword");
        Mockito.when(mockWeapon.level()).thenReturn(1);
    }

    @Test
    void testAddPotionWhenBackpackIsNotFull() {
        backpack.addPotion(mockPotion);
        assertEquals(1, backpack.size(), "Backpack size should be 1 after adding a potion.");
    }

    @Test
    void testAddWeaponWhenBackpackIsNotFull() {
        backpack.addWeapon(mockWeapon);
        assertEquals(1, backpack.size(), "Backpack size should be 1 after adding a weapon.");
    }

    @Test
    void testAddPotionWhenBackpackIsFull() {
        for (int i = 0; i < BackpackImpl.CAPACITY; i++) {
            backpack.addPotion(mockPotion);
        }

        Exception exception = assertThrows(InvalidItemUseException.class, () -> {
            backpack.addPotion(mockPotion);
        });
        assertEquals("Potion cannot be added because backpack is already full", exception.getMessage());
    }

    @Test
    void testAddWeaponWhenBackpackIsFull() {
        for (int i = 0; i < BackpackImpl.CAPACITY; i++) {
            backpack.addWeapon(mockWeapon);
        }

        Exception exception = assertThrows(InvalidItemUseException.class, () -> {
            backpack.addWeapon(mockWeapon);
        });
        assertEquals("Weapon cannot be added because backpack is already full", exception.getMessage());
    }

    @Test
    void testRemovePotionWhenBackpackHasPotions() {
        backpack.addPotion(mockPotion);
        backpack.removePotion(mockPotion);
        assertEquals(0, backpack.size(), "Backpack size should be 0 after removing the potion.");
    }

    @Test
    void testRemovePotionWhenBackpackIsEmpty() {
        Exception exception = assertThrows(InvalidItemUseException.class, () -> {
            backpack.removePotion(mockPotion);
        });
        assertEquals("Potion cannot be removed because there are no potions in the backpack", exception.getMessage());
    }

    @Test
    void testRemoveWeaponWhenBackpackHasWeapons() {
        backpack.addWeapon(mockWeapon);
        backpack.removeWeapon(mockWeapon);
        assertEquals(0, backpack.size(), "Backpack size should be 0 after removing the weapon.");
    }

    @Test
    void testRemoveWeaponWhenBackpackIsEmpty() {
        Exception exception = assertThrows(InvalidItemUseException.class, () -> {
            backpack.removeWeapon(mockWeapon);
        });
        assertEquals("Weapon cannot be removed because there are no weapons in the backpack", exception.getMessage());
    }

    @Test
    void testDropRandomItemWhenBackpackIsEmpty() {
        assertNull(backpack.dropRandomItem(), "Dropping from an empty backpack should return null.");
    }

    @Test
    void testDropRandomItemWhenBackpackHasItems() {
        backpack.addPotion(mockPotion);
        backpack.addWeapon(mockWeapon);

        assertNotNull(backpack.dropRandomItem(), "Dropping a random item from a non-empty backpack should return an item.");
    }
}
