package bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor;

public interface Actor {
    int MAX_LEVEL = 9;

    Stats getStats();

    int getLevel();

    //returns the attack damage that the actor would deal
    int attack();

    //return true if the attackDamage would kill the actor
    boolean receiveAttack(int attackDamage);
}
