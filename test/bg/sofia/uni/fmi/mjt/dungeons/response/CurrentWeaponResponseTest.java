package bg.sofia.uni.fmi.mjt.dungeons.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrentWeaponResponseTest {
    @Test
    void testCurrentWeaponResponseWithValidData() {
        String validWeaponName = "Excalibur";
        int validWeaponLevel = 5;
        CurrentWeaponResponse response = new CurrentWeaponResponse("Success", validWeaponName, validWeaponLevel);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validWeaponName, response.getWeaponName());
        assertEquals(validWeaponLevel, response.getWeaponLevel());
    }

    @Test
    void testCurrentWeaponResponseWithNullWeaponName() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new CurrentWeaponResponse("Error", null, 1));

        assertEquals("Weapon name should not be null", exception.getMessage());
    }

    @Test
    void testCurrentWeaponResponseWithBlankWeaponName() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new CurrentWeaponResponse("Error", " ", 1));

        assertEquals("Weapon name should not be blank", exception.getMessage());
    }

    @Test
    void testCurrentWeaponResponseWithNegativeWeaponLevel() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new CurrentWeaponResponse("Error", "Excalibur", -1));

        assertEquals("Weapon level should not be negative", exception.getMessage());
    }

    @Test
    void testCurrentWeaponResponseWithZeroWeaponLevel() {
        String validWeaponName = "Excalibur";
        int validWeaponLevel = 0;
        CurrentWeaponResponse response = new CurrentWeaponResponse("Success", validWeaponName, validWeaponLevel);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(validWeaponName, response.getWeaponName());
        assertEquals(validWeaponLevel, response.getWeaponLevel());
    }
}
