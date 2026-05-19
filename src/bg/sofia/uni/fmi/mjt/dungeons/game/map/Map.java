package bg.sofia.uni.fmi.mjt.dungeons.game.map;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NonExistentEntityLocationException;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.other.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.BackpackImpl;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.RemainingPlayers;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Map {
    private static final int N = 10;
    private static final int M = 10;
    private static final int NUM_OF_MINIONS = 15;
    private static final int NUM_OF_OBSTACLES = 24;
    private static final int NUM_OF_TREASURES = 9;

    private static final String MINION = "M";
    private static final String OBSTACLE = "#";
    private static final String TREASURE = "T";
    private static final String EMPTY_SPACE = ".";
    private static final int CLUSTER_CELL_LEN = 3;
    private static final String REPLACEMENT = "";
    private static final String SLASH = "/";
    private static final String SLASH_M = "/M";
    private static final String SLASH_T = "/T";
    private static final String MAP_NAME = "MAP: ";
    private static final String THREE_EMPTY_SPACES = "   ";

    private final java.util.Map<Location, Minion> minions;
    private final java.util.Map<Location, Treasure> treasures;
    private final RemainingPlayers remainingPlayers;

    private final List<List<String>> map;

    public Map(RemainingPlayers remainingPlayers) {
        Validator.validateObj(remainingPlayers,
            "Remaining players should not be null");

        this.minions = new HashMap<>();
        this.treasures = new HashMap<>();
        this.remainingPlayers = remainingPlayers;

        map = generateMap();
    }

    public List<List<String>> generateMap() {
        List<List<String>> map = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < M; j++) {
                row.add(EMPTY_SPACE);
            }
            map.add(row);
        }

        Random random = new Random();

        for (int i = 0; i < NUM_OF_OBSTACLES; i++) {
            placeEntity(map, OBSTACLE, random);
        }

        for (int i = 1; i <= remainingPlayers.getInitialNumOfPlayers(); i++) {
            placeEntity(map, String.valueOf(i), random);
        }

        for (int i = 0; i < NUM_OF_MINIONS; i++) {
            placeEntity(map, MINION, random);
        }

        for (int i = 0; i < NUM_OF_TREASURES; i++) {
            placeEntity(map, TREASURE, random);
        }

        return map;
    }

    private void placeEntity(List<List<String>> map, String entityType, Random random) {
        Validator.validateObj(map, "Map should not be null");
        Validator.validateString(entityType,
            "Entity type should not be null",
            "Entity type should not be blank");

        int row;
        int col;

        boolean placed = false;

        while (!placed) {
            row = random.nextInt(N);
            col = random.nextInt(M);

            if (map.get(row).get(col).equals(EMPTY_SPACE)) {
                if (isSurroundedByObstacles(map, row, col) && !entityType.equals(OBSTACLE)) {
                    continue;
                }

                switch (entityType) {
                    case MINION -> minions.put(new Location(row, col), Minion.generateRandomLevelMinion());
                    case TREASURE -> treasures.put(new Location(row, col), Treasure.generateRandomTreasure());
                }

                map.get(row).set(col, entityType);
                placed = true;
            }
        }
    }

    private boolean isSurroundedByObstacles(List<List<String>> map, int row, int col) {
        Validator.validateObj(map, "Map should not be null");
        Validator.validateInt(row, "Row should not be negative");
        Validator.validateInt(col, "Col should not be negative");

        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        int obstacleCount = 0;
        int validNeighbors = 0;

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < M) {
                validNeighbors++;
                if (map.get(newRow).get(newCol).equals(OBSTACLE)) {
                    obstacleCount++;
                }
            }
        }

        return obstacleCount == validNeighbors;
    }

    public List<List<String>> getMap() {
        return map;
    }

    public static void printMap(List<List<String>> map) {
        Validator.validateObj(map, "Map should not be null");

        System.out.println(System.lineSeparator() + MAP_NAME);
        for (List<String> row: map) {
            for (String entity: row) {
                System.out.print(entity + THREE_EMPTY_SPACES);
            }
            System.out.println();
        }
    }

    public java.util.Map<Location, Minion> getMinions() {
        return minions;
    }

    public java.util.Map<Location, Treasure> getTreasures() {
        return treasures;
    }

    public boolean updateMapMovePlayer(int playerID, DIRECTION direction) {
        Validator.validatePlayerID(playerID);
        Validator.validateObj(direction, "Direction should not be null");
        Location currentLocation = findPlayerLocation(playerID);
        if (currentLocation == null) {
            throw new NonExistentEntityLocationException(
                "Player with ID " + playerID + " should be on the map");
        }
        int row = currentLocation.i();
        int col = currentLocation.j();
        switch (direction) {
            case UP -> row -= 1;
            case DOWN -> row += 1;
            case LEFT -> col -= 1;
            case RIGHT -> col += 1;
            default -> {
                return false;
            }
        }
        if (row < 0 || row >= N || col < 0 || col >= M) {
            return false;
        }
        String targetCell = map.get(row).get(col);
        if (!updateMap(targetCell, playerID, row, col)) {
            return false;
        }
        String updatedCell = updateCell(playerID, currentLocation);
        map.get(currentLocation.i()).set(currentLocation.j(), updatedCell);
        return true;
    }

    public int updateMapRemovePlayer(int playerID) {
        Validator.validatePlayerID(playerID);
        Location currentLocation = findPlayerLocation(playerID);
        if (currentLocation == null) {
            throw new NonExistentEntityLocationException(
                "Player with ID " + playerID + " should be on the map");
        }

        String updatedCell = updateCell(playerID, currentLocation);
        map.get(currentLocation.i()).set(currentLocation.j(), updatedCell);

        Player player = remainingPlayers.getPlayer(playerID);
        BackpackImpl playerBackpack = player.getBackpack();
        Treasure droppedItem = playerBackpack.dropRandomItem();

        if (droppedItem != null) {
            Location dropLocation = findValidAdjacentTile(currentLocation);
            if (dropLocation != null) {
                map.get(dropLocation.i()).set(dropLocation.j(), TREASURE);
                treasures.put(dropLocation, droppedItem);
            }
        }

        return player.getLevel();
    }

    public Treasure updateMapRemoveTreasure(Location location) {
        Validator.validateObj(location, "Location should not be null");

        int row = location.i();
        int col = location.j();

        String currCell = map.get(row).get(col);
        if (!currCell.contains(SLASH_T) && !currCell.equals(TREASURE)) {
            throw new NonExistentEntityLocationException(
                "The location passed as a parameter should be that of a treasure");
        }

        String updatedCell = EMPTY_SPACE;
        if (currCell.contains(SLASH_T)) {
            updatedCell = currCell.replace(SLASH_T, REPLACEMENT);
        }

        map.get(row).set(col, updatedCell);

        Treasure treasure = treasures.get(location);
        treasures.remove(location);

        placeEntity(map, TREASURE, new Random());

        return treasure;
    }

    public int updateMapRemoveMinion(Location location) {
        Validator.validateObj(location, "Location should not be null");

        int row = location.i();
        int col = location.j();

        String currCell = map.get(row).get(col);
        if (!currCell.contains(SLASH_M) && !currCell.equals(MINION)) {
            throw new NonExistentEntityLocationException(
                "The location passed as a parameter should be that of a minion");
        }

        String updatedCell = EMPTY_SPACE;
        if (currCell.contains(SLASH_M)) {
            updatedCell = currCell.replace(SLASH_M, REPLACEMENT);
        }

        map.get(row).set(col, updatedCell);

        Minion minion = minions.get(location);
        minions.remove(location);

        placeEntity(map, MINION, new Random());
        return minion.getLevel();
    }

    Location findValidAdjacentTile(Location currentLocation) {
        Validator.validateObj(currentLocation, "Current location should not be null");

        int row = currentLocation.i();
        int col = currentLocation.j();

        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < M) {
                String targetCell = map.get(newRow).get(newCol);

                if (targetCell.equals(EMPTY_SPACE)) {
                    return new Location(newRow, newCol);
                }
            }
        }

        return null;
    }

    private boolean updateMap(String targetCell, int playerID, int row, int col) {
        Validator.validateString(targetCell,
            "Target cell should not be null",
            "Target cell should not be blank");
        Validator.validatePlayerID(playerID);
        Validator.validateInt(row, "Row should not be negative");
        Validator.validateInt(col, "Col should not be negative");

        if (targetCell.equals(OBSTACLE) || targetCell.length() == CLUSTER_CELL_LEN) {
            return false;
        }

        if (targetCell.equals(MINION)) {
            map.get(row).set(col, playerID + SLASH_M);
        } else if (targetCell.equals(TREASURE)) {
            map.get(row).set(col, playerID + SLASH_T);
        } else if (Character.isDigit(targetCell.charAt(0))) {
            int otherPlayerID = Integer.parseInt(targetCell);
            if (otherPlayerID != playerID) {
                map.get(row).set(col, playerID + SLASH + otherPlayerID);
            }
        } else {
            map.get(row).set(col, String.valueOf(playerID));
        }

        return true;
    }

    private String updateCell(int playerID, Location currentLocation) {
        Validator.validatePlayerID(playerID);
        Validator.validateObj(currentLocation, "Current location should not be null");
        String previousCell = map.get(currentLocation.i()).get(currentLocation.j());
        String updatedCell = EMPTY_SPACE;

        if (previousCell.contains(MINION)) {
            updatedCell = MINION;
        } else if (previousCell.contains(TREASURE)) {
            updatedCell = TREASURE;
        } else if (previousCell.length() == CLUSTER_CELL_LEN) {
            updatedCell = previousCell
                .replace(String.valueOf(playerID), REPLACEMENT)
                .replace(SLASH, REPLACEMENT);
        }
        return updatedCell;
    }

    public Location findPlayerLocation(int playerID) {
        Validator.validatePlayerID(playerID);

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                String cell = map.get(row).get(col);
                if (cell.contains(String.valueOf(playerID))) {
                    return new Location(row, col);
                }
            }
        }
        return null;
    }
}
