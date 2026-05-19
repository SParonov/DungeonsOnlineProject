package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidItemUseException;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Sword;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = Player.initialize();
    }

    @Test
    void testInitialize() {
        assertNotNull(player.getStats(), "Player's stats should be initialized.");
        assertEquals(0, player.getLevel(), "Player's level should be initialized to 0.");
        assertEquals(15, player.getExpForNextLevel(), "Player's experience for the next level should be initialized to 0.");
        assertNotNull(player.getBackpack(), "Player's backpack should be initialized.");
        assertNotNull(player.getCurrWeapon(), "Player's current weapon should be initialized.");
    }

    @Test
    void testAttack() {
        int attack = player.attack();
        assertEquals(player.getStats().getAttack() + player.getCurrWeapon().attack(), attack, "Attack value should be the sum of the player's attack and weapon's attack.");
    }

    @Test
    void testReceiveAttack() {
        int initialHealth = player.getStats().getHealth();
        boolean isDead = player.receiveAttack(10);

        assertFalse(isDead, "Player should not be dead after receiving a small attack.");
        assertEquals(99, player.getStats().getHealth(), "Player's health should not decrease by the damage.");

        isDead = player.receiveAttack(10000);
        assertTrue(isDead, "Player should be dead after receiving a large attack.");
    }

    @Test
    void testLevelUp() {
        player.receiveExp(1);

        assertEquals(0, player.getLevel(), "Player's level should remain 1 after receiving insufficient experience.");

        player.receiveExp(1000);

        assertEquals(1, player.getLevel(), "Player should level up after receiving sufficient experience.");
        assertEquals(30, player.getExpForNextLevel(), "Player's experience needed for the next level should increase after leveling up.");
    }

    @Test
    void testReceiveExp() {
        assertTrue(player.receiveExp(1), "Player should receive experience and not level up prematurely.");
        assertEquals(10, player.getExpForNextLevel(), "Player's remaining exp for next level should be reduced correctly.");
    }

    @Test
    void testChangeWeapon() {
        Weapon newWeapon = new Sword(1);

        assertThrows(InvalidItemUseException.class, () -> player.changeWeapon(newWeapon), "Player's current weapon should not be updated.");
    }

    @Test
    void testUsePotion() {
        Potion healthPotion = new HealthPotion();
        player.getBackpack().addPotion(healthPotion);

        boolean usedPotion = player.usePotion(healthPotion);
        assertTrue(usedPotion, "Potion should be used successfully.");
        assertEquals(player.getStats().getHealth(), 100, "Player's health should be increased after using a health potion.");
        assertEquals(25, player.getStats().getMana(), "Player's mana should be decreased after using a health potion.");
        assertEquals(0, player.getBackpack().getPotions().size(), "Potion should be removed from the backpack after use.");
    }

    @Test
    void testFailToUsePotionDueToInsufficientMana() {
        Potion manaPotion = new ManaPotion();
        player.getBackpack().addPotion(manaPotion);
        player.getStats().setMana(10);

        boolean usedPotion = player.usePotion(manaPotion);

        assertFalse(usedPotion, "Potion should not be used if there is insufficient mana.");
    }

    @Test
    void testUsePotionWithNegativeMana() {
        Potion manaPotion = new ManaPotion();
        player.getBackpack().addPotion(manaPotion);

        assertThrows(IllegalArgumentException.class, () -> player.getStats().setMana(-10), "Mana should not be negative.");
    }
}
