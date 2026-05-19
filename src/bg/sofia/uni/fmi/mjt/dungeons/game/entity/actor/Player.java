package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

import bg.sofia.uni.fmi.mjt.dungeons.game.item.BackpackImpl;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Hand;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

public class Player implements Actor {

    private static final int INITIAL_HEALTH = 100;
    private static final int INITIAL_MANA = 100;
    private static final int INITIAL_ATTACK = 50;
    private static final int INITIAL_DEFENSE = 50;
    private static final int INITIAL_LEVEL = 0;
    private static final int INITIAL_EXP = 15;
    private static final double EXP_MULTI = 2;
    private static final int RECEIVED_EXP_MULTI = 5;

    private final Stats stats;
    private final BackpackImpl backpack;
    private Weapon currWeapon;
    private int level;
    private int expForNextLevel;
    private int remainingExpForNextLevel;

    private Player(int health, int mana, int attack, int defense) {
        this.stats = new Stats(health, mana, attack, defense);
        this.level = INITIAL_LEVEL;
        this.expForNextLevel = INITIAL_EXP;
        this.remainingExpForNextLevel = INITIAL_EXP;
        this.backpack = new BackpackImpl();
        this.currWeapon = new Hand();
    }

    public static Player initialize() {
        return new Player(INITIAL_HEALTH, INITIAL_MANA, INITIAL_ATTACK, INITIAL_DEFENSE);
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
        return stats.getAttack() + currWeapon.attack();
    }

    @Override
    public boolean receiveAttack(int attackDamage) {
        Validator.validateInt(attackDamage, "Attack damage should not be negative");

        int actualDamage = Math.max(attackDamage - stats.getDefense(), 1);

        if (stats.getHealth() - actualDamage <= 0) {
            return true;
        }

        stats.setHealth(stats.getHealth() - actualDamage);
        return false;
    }

    public boolean levelUp() {
        if (level == Actor.MAX_LEVEL) {
            return false;
        }
        stats.levelUp();
        expForNextLevel = (int)Math.floor(expForNextLevel * EXP_MULTI);
        remainingExpForNextLevel = expForNextLevel;
        level++;
        return true;
    }

    public boolean receiveExp(int level) {
        Validator.validateInt(level, "Level should not be negative");

        int exp = level * RECEIVED_EXP_MULTI;
        int diff = remainingExpForNextLevel - exp;

        if (diff > 0) {
            remainingExpForNextLevel -= exp;
            return true;
        } else {
            return levelUp();
        }
    }

    public int getExpForNextLevel() {
        return remainingExpForNextLevel;
    }

    public BackpackImpl getBackpack() {
        return backpack;
    }

    public Weapon getCurrWeapon() {
        return currWeapon;
    }

    public void changeWeapon(Weapon weapon) {
        Validator.validateObj(weapon, "Weapon should not be null");

        backpack.removeWeapon(weapon);
        Weapon prevCurr = currWeapon;
        currWeapon = weapon;

        if (currWeapon.level() == 0) {
            return;
        }

        backpack.addWeapon(prevCurr);
    }

    public boolean usePotion(Potion potion) {
        Validator.validateObj(potion, "Potion should not be null");

        if (potion.getManaCost() > stats.getMana()) {
            return false;
        }

        potion.consume(stats);
        backpack.removePotion(potion);

        return true;
    }
}
