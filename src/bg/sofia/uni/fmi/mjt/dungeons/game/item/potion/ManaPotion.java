package bg.sofia.uni.fmi.mjt.dungeons.game.item.potion;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

public class ManaPotion implements Potion {
    public static final String NAME = "MANA POTION";
    private static final int MANA_COST = 15;
    private static final int MANA_RESTORE = 100;

    @Override
    public void consume(Stats stats) {
        Validator.validateObj(stats, "Stats should not be null");

        stats.setMana(stats.getMana() - MANA_COST);
        stats.setMana(stats.getMana() + MANA_RESTORE);
    }

    @Override
    public int getManaCost() {
        return MANA_COST;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
