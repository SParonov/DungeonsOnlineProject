package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

public class Hand implements Weapon {
    public static final String NAME = "HAND";
    private static final int BASE_DAMAGE = 15;
    private static final int HAND_LEVEL = 0;

    @Override
    public int level() {
        return HAND_LEVEL;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int attack() {
        return BASE_DAMAGE;
    }
}
