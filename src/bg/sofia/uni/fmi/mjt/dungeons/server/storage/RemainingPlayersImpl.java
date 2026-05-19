package bg.sofia.uni.fmi.mjt.dungeons.server.storage;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidContainerAccessException;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.util.Map;
import java.util.TreeMap;

public class RemainingPlayersImpl implements RemainingPlayers {

    private static int currID = 1;

    private final int numOfPlayers;
    private final Map<Integer, Player> playersMap;

    public RemainingPlayersImpl(int numOfPlayers) {
        if (numOfPlayers == 0) {
            throw new IllegalArgumentException("Num of players should not be zero");
        }

        Validator.validateInt(numOfPlayers, "Num of players should be positive");

        this.numOfPlayers = numOfPlayers;
        this.playersMap = new TreeMap<>();
    }

    @Override
    public void removePlayer(int playerID) {
        Validator.validatePlayerID(playerID);

        playersMap.remove(playerID);
    }

    @Override
    public int addPlayer() {
        playersMap.put(currID, Player.initialize());

        return currID++;
    }

    @Override
    public boolean hasOneLeft() {
        return playersMap.size() == 1;
    }

    @Override
    public boolean isFull() {
        return currID == numOfPlayers + 1;
    }

    @Override
    public boolean hasPlayer(int playerID) {
        Validator.validatePlayerID(playerID);

        return playersMap.containsKey(playerID);
    }

    @Override
    public int getRemainingPlayerID() {
        if (!hasOneLeft()) {
            throw new InvalidContainerAccessException(
                "This method should be called only when there is only one player left");
        }

        return playersMap.keySet().iterator().next();
    }

    public int getInitialNumOfPlayers() {
        return numOfPlayers;
    }

    @Override
    public Player getPlayer(int playerID) {
        Validator.validatePlayerID(playerID);

        return playersMap.get(playerID);
    }
}
