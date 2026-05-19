package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

public class MessageResponse extends Response {
    @SerializedName("message") private final String message;

    public MessageResponse(String status, String message) {
        super(status);

        Validator.validateString(message,
            "Message should not be null",
            "Message should not be blank");

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
