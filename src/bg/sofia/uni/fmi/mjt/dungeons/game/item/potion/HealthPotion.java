package bg.sofia.uni.fmi.mjt.dungeons.game.item.potion;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

public class HealthPotion implements Potion {
    public static final String NAME = "HEALTH POTION";
    private static final int MANA_COST = 75;
    private static final int HEALTH_HEAL = 50;

    @Override
    public void consume(Stats stats) {
        Validator.validateObj(stats, "Stats should not be null");

        stats.setMana(stats.getMana() - MANA_COST);
        stats.setHealth(stats.getHealth() + HEALTH_HEAL);
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
