package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

public class Stats {
    private static final int LEVEL_UP_HEALTH = 10;
    private static final int LEVEL_UP_MANA = 10;
    private static final int LEVEL_UP_ATTACK = 5;
    private static final int LEVEL_UP_DEFENSE = 5;
    private static final String HEALTH_TEXT = "HEALTH: ";
    private static final String MANA_TEXT = "MANA: ";
    private static final String ATTACK_TEXT = "ATTACK: ";
    private static final String DEFENSE_TEXT = "DEFENSE: ";

    private int health;
    private int mana;
    private int attack;
    private int defense;

    private int maxHealth;
    private int maxMana;

    public Stats(int health, int mana, int attack, int defense) {
        Validator.validateInt(health, "Health should not be negative");
        Validator.validateInt(mana, "Mana should not be negative");
        Validator.validateInt(attack, "Attack should not be negative");
        Validator.validateInt(defense, "Defense should not be negative");

        this.maxHealth = health;
        this.maxMana = mana;

        this.health = maxHealth;
        this.mana = maxMana;
        this.attack = attack;
        this.defense = defense;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setHealth(int health) {
        Validator.validateInt(health, "Health should not be negative");

        if (health > maxHealth) {
            this.health = maxHealth;
            return;
        }

        this.health = health;
    }

    public void setMana(int mana) {
        Validator.validateInt(mana, "Mana should not be negative");

        if (mana > maxMana) {
            this.mana = maxMana;
            return;
        }
        this.mana = mana;
    }

    public void levelUp() {
        maxHealth += LEVEL_UP_HEALTH;
        maxMana += LEVEL_UP_MANA;
        attack += LEVEL_UP_ATTACK;
        defense += LEVEL_UP_DEFENSE;
        health = maxHealth;
        mana = maxMana;
    }

    public void printStats() {
        System.out.println(HEALTH_TEXT + health);
        System.out.println(MANA_TEXT + mana);
        System.out.println(ATTACK_TEXT + attack);
        System.out.println(DEFENSE_TEXT + defense);
    }
}
