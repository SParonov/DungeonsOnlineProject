package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

public class BackpackResponse extends Response {
    @SerializedName("backpack") private final String backpackContent;

    public BackpackResponse(String status, String backpackContent) {
        super(status);

        Validator.validateString(backpackContent,
            "Backpack content should not be null",
            "Backpack content should not be blank");

        this.backpackContent = backpackContent;
    }

    public String getBackpackContent() {
        return backpackContent;
    }
}
