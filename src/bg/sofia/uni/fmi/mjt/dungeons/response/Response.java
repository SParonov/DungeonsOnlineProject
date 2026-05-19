package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @SerializedName("status") protected final String status;

    @Serial
    private static final long serialVersionUID = 5328226322941459894L;

    public Response(String status) {
        Validator.validateString(status,
            "Status should not be null",
            "Status should not be blank");

        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
