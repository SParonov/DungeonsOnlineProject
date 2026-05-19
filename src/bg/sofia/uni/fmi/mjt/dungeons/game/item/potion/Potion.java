package bg.sofia.uni.fmi.mjt.dungeons.game.item.potion;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;

public interface Potion {
    void consume(Stats stats);

    int getManaCost();

    String getName();
}
