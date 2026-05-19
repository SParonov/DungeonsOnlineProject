package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

public record Sword(int level) implements Weapon {
    public static final String NAME = "SWORD";
    private static final int BASE_DAMAGE = 20;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int attack() {
        return level * BASE_DAMAGE;
    }
}
