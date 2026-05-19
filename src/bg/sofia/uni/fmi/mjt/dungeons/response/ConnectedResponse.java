package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

public class ConnectedResponse extends Response {

    @SerializedName("playerID") private final int playerID;
    @SerializedName("message") private final String message;

    public ConnectedResponse(String status, String message, int playerID) {
        super(status);

        Validator.validateString(message,
            "Message should not be null",
            "Message should not be blank");

        Validator.validatePlayerID(playerID);

        this.message = message;
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public String getMessage() {
        return message;
    }
}
