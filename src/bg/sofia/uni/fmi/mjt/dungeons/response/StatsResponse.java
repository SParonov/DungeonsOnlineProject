package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

public class StatsResponse extends Response {
    @SerializedName("stats") private final Stats stats;

    public StatsResponse(String status, Stats stats) {
        super(status);

        Validator.validateObj(stats, "Stats should no be null");

        this.stats = stats;
    }

    public Stats getStats() {
        return stats;
    }
}
