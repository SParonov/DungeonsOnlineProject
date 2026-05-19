package bg.sofia.uni.fmi.mjt.dungeons.game.item;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.other.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;

import java.util.List;

public interface Backpack {
    boolean isFull();

    int size();

    void addPotion(Potion potion);

    void addWeapon(Weapon weapon);

    void removePotion(Potion potion);

    void removeWeapon(Weapon weapon);

    List<Potion> getPotions();

    List<Weapon> getWeapons();

    Treasure dropRandomItem();
}
