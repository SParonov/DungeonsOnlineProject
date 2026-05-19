package bg.sofia.uni.fmi.mjt.dungeons.game.map;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

public record Location(int i, int j) {
    public Location {
        Validator.validateInt(i, "i should not be negative");
        Validator.validateInt(j, "j should not be negative");
    }
}
