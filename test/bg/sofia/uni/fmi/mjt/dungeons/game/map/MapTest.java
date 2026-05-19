package bg.sofia.uni.fmi.mjt.dungeons.game.map;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.other.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.RemainingPlayers;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.RemainingPlayersImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTest {
    private final RemainingPlayers remainingPlayers = new RemainingPlayersImpl(3);

    @Test
    void testMapInitialization() {
        Map map = new Map(remainingPlayers);

        assertEquals(10, map.getMap().size(), "Map should have 10 rows.");
        assertEquals(10, map.getMap().getFirst().size(), "Each row should have 10 columns.");

        Map.printMap(List.of());
    }

    @Test
    void testFindValidAdjacentTile_emptySpace() {
        Map map = new Map(remainingPlayers);
        map.generateMap();

        Location playerLocation = map.findPlayerLocation(1);
        assertNotNull(playerLocation, "Player 1 should be placed on the map.");

        Location validAdjacent = map.findValidAdjacentTile(playerLocation);

        assertNotNull(validAdjacent, "There should be a valid adjacent empty space.");
        assertEquals(".", map.getMap().get(validAdjacent.i()).get(validAdjacent.j()),
            "The adjacent tile should be empty.");
    }

    @Test
    void testEntityPlacement() {
        Map map = new Map(remainingPlayers);

        assertFalse(map.getMinions().isEmpty(), "Minions should be placed after map is generated.");
        assertFalse(map.getTreasures().isEmpty(), "Treasures should be placed after map is generated.");

        map.generateMap();

        assertFalse(map.getMinions().isEmpty(), "Minions should be placed on the map.");
        assertFalse(map.getTreasures().isEmpty(), "Treasures should be placed on the map.");
    }

    @Test
    void testPlayerMovement() {
        Map map = new Map(remainingPlayers);
        map.generateMap();

        Location initialLocation = map.findPlayerLocation(1);

        boolean moved = map.updateMapMovePlayer(1, DIRECTION.UP);
        assertTrue(moved, "Player should be able to move up.");

        Location newLocation = map.findPlayerLocation(1);
        assertNotEquals(initialLocation, newLocation, "Player should have moved.");

        moved = map.updateMapMovePlayer(1, DIRECTION.LEFT);
        assertFalse(moved, "Player should not be able to move into an invalid location.");
    }

    @Test
    void testUpdateMapRemoveMinion() {
        Map map = new Map(remainingPlayers);
        map.generateMap();

        Location minionLocation = map.getMinions().keySet().iterator().next();
        int levelBefore = map.getMinions().get(minionLocation).getLevel();
        int levelAfter = map.updateMapRemoveMinion(minionLocation);

        assertEquals(levelBefore, levelAfter, "Minion level should not change after removal.");
    }

    @Test
    void testUpdateMapRemoveTreasure() {
        Map map = new Map(remainingPlayers);
        map.generateMap();

        Location treasureLocation = map.getTreasures().keySet().iterator().next();
        Treasure treasureBefore = map.getTreasures().get(treasureLocation);
        Treasure treasureAfter = map.updateMapRemoveTreasure(treasureLocation);

        assertEquals(treasureBefore, treasureAfter, "Treasure should be replaced after removal.");
    }
}
