package bg.sofia.uni.fmi.mjt.dungeons.response;

import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.annotations.SerializedName;

public class CurrentWeaponResponse extends Response {
    @SerializedName("weaponName") private final String weaponName;
    @SerializedName("weaponLevel") private final int weaponLevel;

    public CurrentWeaponResponse(String status, String weaponName, int weaponLevel) {
        super(status);

        Validator.validateString(weaponName,
            "Weapon name should not be null",
            "Weapon name should not be blank");
        Validator.validateInt(weaponLevel, "Weapon level should not be negative");

        this.weaponName = weaponName;
        this.weaponLevel = weaponLevel;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }
}
