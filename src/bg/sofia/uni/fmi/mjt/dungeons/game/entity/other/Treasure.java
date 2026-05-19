package bg.sofia.uni.fmi.mjt.dungeons.game.entity.other;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidItemUseException;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Dagger;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Mace;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Sword;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.util.Random;

public class Treasure {
    private static final int MAX_LEVEL = 9;
    private static final int NUM_OF_WEAPONS = 3;
    private static final int DAGGER_NUM = 0;
    private static final int SWORD_NUM = 1;
    private static final int MACE_NUM = 2;

    private Potion randomPotion;
    private Weapon randomWeapon;

    public Treasure(Potion randomPotion, Weapon randomWeapon) {
        Validator.validateObj(randomPotion, "Random potion should not be null");
        Validator.validateObj(randomWeapon, "Random weapon should not be null");

        this.randomPotion = randomPotion;
        this.randomWeapon = randomWeapon;
    }

    public Treasure(Potion randomPotion) {
        Validator.validateObj(randomPotion, "Random potion should not be null");
        this.randomPotion = randomPotion;
    }

    public Treasure(Weapon randomWeapon) {
        Validator.validateObj(randomWeapon, "Random weapon should not be null");
        this.randomWeapon = randomWeapon;
    }

    public boolean hasPotion() {
        return randomPotion != null;
    }

    public boolean hasWeapon() {
        return randomWeapon != null;
    }

    public Potion getRandomPotion() {
        return randomPotion;
    }

    public Weapon getRandomWeapon() {
        return randomWeapon;
    }

    public static Treasure generateRandomTreasure() {
        Random random = new Random();

        boolean hasPotion = random.nextBoolean();
        boolean hasWeapon = random.nextBoolean();

        if (!hasPotion && !hasWeapon) {
            hasPotion = true;
        }

        Potion potion = hasPotion ? generateRandomPotion(random) : null;
        Weapon weapon = hasWeapon ? generateRandomWeapon(random) : null;

        if (potion != null && weapon != null) {
            return new Treasure(potion, weapon);
        } else if (potion != null) {
            return new Treasure(potion);
        } else {
            return new Treasure(weapon);
        }
    }

    private static Potion generateRandomPotion(Random random) {
        return random.nextBoolean() ? new HealthPotion() : new ManaPotion();
    }

    private static Weapon generateRandomWeapon(Random random) {
        int level = random.nextInt(MAX_LEVEL) + 1;
        int weaponType = random.nextInt(NUM_OF_WEAPONS);

        return switch (weaponType) {
            case DAGGER_NUM -> new Dagger(level);
            case SWORD_NUM -> new Sword(level);
            case MACE_NUM -> new Mace(level);
            default -> throw new InvalidItemUseException("Unexpected value: " + weaponType);
        };
    }
}
