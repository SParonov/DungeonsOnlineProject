package bg.sofia.uni.fmi.mjt.dungeons.request;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

public record Request(@SerializedName("command") String command,
                      @SerializedName("playerID") int playerID) {
    public Request {
        Validator.validateObj(command, "Command should not be null");

        Validator.validatePlayerID(playerID);
    }
}
