package bg.sofia.uni.fmi.mjt.dungeons.server.storage;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidContainerAccessException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemainingPlayersImplTest {
    private RemainingPlayersImpl remainingPlayers;

    @BeforeEach
    void setUp() {
        remainingPlayers = new RemainingPlayersImpl(3);
    }

    @Test
    void testConstructorWithValidNumOfPlayers() {
        assertDoesNotThrow(() -> new RemainingPlayersImpl(3),
            "Valid number of players should not throw an exception");
    }

    @Test
    void testConstructorWithZeroPlayers() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new RemainingPlayersImpl(0));
        assertEquals("Num of players should not be zero", exception.getMessage(),
            "Constructor should throw IllegalArgumentException for zero players");
    }

    @Test
    void testConstructorWithNegativePlayers() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new RemainingPlayersImpl(-5));
        assertEquals("Num of players should be positive", exception.getMessage(),
            "Constructor should throw IllegalArgumentException for negative players");
    }

    @Test
    void testRemovePlayer() {
        int player1 = remainingPlayers.addPlayer();
        remainingPlayers.removePlayer(player1);

        assertFalse(remainingPlayers.hasPlayer(player1), "Player should be removed successfully");
    }

    @Test
    void testRemovePlayerWithInvalidID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> remainingPlayers.removePlayer(10)); // Out-of-range ID
        assertEquals("Player ID should be between 1 and 9", exception.getMessage(),
            "Removing an invalid player ID should throw an InvalidPlayerIDException");
    }

    @Test
    void testHasOneLeftWithMultiplePlayers() {
        remainingPlayers.addPlayer();
        remainingPlayers.addPlayer();
        remainingPlayers.addPlayer();

        assertFalse(remainingPlayers.hasOneLeft(), "Should return false when multiple players exist");
    }


    @Test
    void testIsFullWithPartialPlayers() {
        remainingPlayers.addPlayer();
        assertFalse(remainingPlayers.isFull(), "Should return false when the game is not full");
    }

    @Test
    void testGetRemainingPlayerIDWithOneLeft() {
        int p1 = remainingPlayers.addPlayer();
        int p2 = remainingPlayers.addPlayer();
        int p3 = remainingPlayers.addPlayer();

        remainingPlayers.removePlayer(p1);
        remainingPlayers.removePlayer(p2);

        assertEquals(p3, remainingPlayers.getRemainingPlayerID(),
            "Should return the ID of the last remaining player");
    }

    @Test
    void testGetRemainingPlayerIDWithoutOnePlayerLeft() {
        remainingPlayers.addPlayer();
        remainingPlayers.addPlayer();

        Exception exception = assertThrows(InvalidContainerAccessException.class,
            () -> remainingPlayers.getRemainingPlayerID());
        assertEquals("This method should be called only when there is only one player left",
            exception.getMessage(),
            "Should throw InvalidContainerAccessException when multiple players remain");
    }

    @Test
    void testGetPlayerWithInvalidID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> remainingPlayers.getPlayer(10));
        assertEquals("Player ID should be between 1 and 9", exception.getMessage(),
            "Should throw InvalidPlayerIDException for an invalid player ID");
    }

    @Test
    void testGetInitialNumOfPlayers() {
        assertEquals(3, remainingPlayers.getInitialNumOfPlayers(),
            "getInitialNumOfPlayers() should return the initially set number of players");
    }
}
