package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapResponse extends Response {
    @SerializedName("map") private final List<List<String>> map;

    public MapResponse(String status, List<List<String>> map) {
        super(status);

        Validator.validateObj(map, "Map should not be null");
        for (List<String> row: map) {
            Validator.validateObj(row, "Row should not be null");
        }

        this.map = map;
    }

    public List<List<String>> getMap() {
        return map;
    }
}
