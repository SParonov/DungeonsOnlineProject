package bg.sofia.uni.fmi.mjt.dungeons.server.storage;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;

public interface RemainingPlayers {

    void removePlayer(int playerID);

    int addPlayer();

    boolean hasOneLeft();

    boolean isFull();

    boolean hasPlayer(int playerID);

    int getRemainingPlayerID();

    int getInitialNumOfPlayers();

    Player getPlayer(int playerID);
}
