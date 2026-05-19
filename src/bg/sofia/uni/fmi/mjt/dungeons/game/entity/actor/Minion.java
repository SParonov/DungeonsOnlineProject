package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.util.Random;

public class Minion implements Actor {

    private static final int LOWEST_BOUNDARY = 40;
    private static final int ADD_TO_BOUNDARY = 10;
    private static final int MIN_STAT_VAL = 10;

    private final Stats stats;
    private final int level;

    private Minion(int health, int mana, int attack, int defense, int level) {
        this.stats = new Stats(health, mana, attack, defense);
        this.level = level;
    }

    private static int getBoundary(int level) {
        if (level < 1 || level > Actor.MAX_LEVEL) {
            throw new IllegalArgumentException("Level must be between 1 and " + Actor.MAX_LEVEL);
        }

        return LOWEST_BOUNDARY + (level - 1) * ADD_TO_BOUNDARY;
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int attack() {
        return stats.getAttack();
    }

    @Override
    public boolean receiveAttack(int attackDamage) {
        Validator.validateInt(attackDamage, "Attack damage should not be negative");

        int actualDamage = attackDamage - stats.getDefense();

        if (stats.getHealth() - actualDamage <= 0) {
            return true;
        }

        stats.setHealth(stats.getHealth() - actualDamage);
        return false;
    }

    public static Minion generateRandomLevelMinion() {
        Random random = new Random();
        int level = random.nextInt(Actor.MAX_LEVEL) + 1;
        int boundary = getBoundary(level);

        int health = random.nextInt(boundary - MIN_STAT_VAL + 1) + MIN_STAT_VAL;
        int mana = random.nextInt(boundary - MIN_STAT_VAL + 1) + MIN_STAT_VAL;
        int attack = random.nextInt(boundary - MIN_STAT_VAL + 1) + MIN_STAT_VAL;
        int defense = random.nextInt(boundary - MIN_STAT_VAL + 1) + MIN_STAT_VAL;

        return new Minion(health, mana, attack, defense, level);
    }
}
