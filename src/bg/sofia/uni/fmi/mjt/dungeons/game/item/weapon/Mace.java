package bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon;

public record Mace(int level) implements Weapon {
    public static final String NAME = "MACE";
    private static final int BASE_DAMAGE = 25;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int attack() {
        return level * BASE_DAMAGE;
    }
}
