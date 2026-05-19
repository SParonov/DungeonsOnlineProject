package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

public record Dagger(int level) implements Weapon {
    public static final String NAME = "DAGGER";
    private static final int BASE_DAMAGE = 15;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int attack() {
        return level * BASE_DAMAGE;
    }
}
