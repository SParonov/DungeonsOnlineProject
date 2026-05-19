package bg.sofia.uni.fmi.mjt.dungeons.game.item;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidItemUseException;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.other.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BackpackImpl implements Backpack {
    public static final int CAPACITY = 10;
    private static final String TEXT = ", level ";

    ArrayList<Potion> potions;
    ArrayList<Weapon> weapons;

    public BackpackImpl() {
        this.potions = new ArrayList<>();
        this.weapons = new ArrayList<>();
    }

    @Override
    public boolean isFull() {
        return (potions.size() + weapons.size()) == CAPACITY;
    }

    @Override
    public int size() {
        return potions.size() + weapons.size();
    }

    @Override
    public void addPotion(Potion potion) {
        Validator.validateObj(potion, "Potion should not be null");

        if (isFull()) {
            throw new InvalidItemUseException(
                "Potion cannot be added because backpack is already full");
        }
        potions.add(potion);
    }

    @Override
    public void addWeapon(Weapon weapon) {
        Validator.validateObj(weapon, "Weapon should not be null");

        if (isFull()) {
            throw new InvalidItemUseException(
                "Weapon cannot be added because backpack is already full");
        }
        weapons.add(weapon);
    }

    @Override
    public void removePotion(Potion potion) {
        Validator.validateObj(potion, "Potion should not be null");
        if (potions.isEmpty()) {
            throw new InvalidItemUseException(
                "Potion cannot be removed because there are no potions in the backpack");
        }
        potions.remove(potion);
    }

    @Override
    public void removeWeapon(Weapon weapon) {
        Validator.validateObj(weapon, "Weapon should not be null");

        if (weapons.isEmpty()) {
            throw new InvalidItemUseException(
                "Weapon cannot be removed because there are no weapons in the backpack");
        }
        weapons.remove(weapon);
    }

    @Override
    public List<Potion> getPotions() {
        return List.copyOf(potions);
    }

    @Override
    public List<Weapon> getWeapons() {
        return List.copyOf(weapons);
    }

    @Override
    public Treasure dropRandomItem() {
        if (potions.isEmpty() && weapons.isEmpty()) {
            return null;
        }

        Random random = new Random();

        boolean dropPotion = !potions.isEmpty() && (weapons.isEmpty() || random.nextBoolean());

        if (dropPotion) {
            Potion removedPotion = potions.remove(random.nextInt(potions.size()));
            return new Treasure(removedPotion);
        } else {
            Weapon removedWeapon = weapons.remove(random.nextInt(weapons.size()));
            return new Treasure(removedWeapon);
        }
    }

    public String getPrintableBackpack() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contents of your backpack:")
            .append(System.lineSeparator());

        sb.append("Potions:").append(System.lineSeparator());
        if (potions.isEmpty()) {
            sb.append("none").append(System.lineSeparator());
        } else {
            for (Potion potion : potions) {
                sb.append(potion.getName())
                    .append(System.lineSeparator());
            }
        }

        sb.append("Weapons:").append(System.lineSeparator());
        if (weapons.isEmpty()) {
            sb.append("none").append(System.lineSeparator());
        } else {
            for (Weapon weapon : weapons) {
                sb.append(weapon.getName())
                    .append(TEXT)
                    .append(weapon.level())
                    .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
