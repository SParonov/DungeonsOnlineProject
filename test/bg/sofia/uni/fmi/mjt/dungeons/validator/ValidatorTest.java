package bg.sofia.uni.fmi.mjt.dungeons.validator;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {
    @Test
    void testValidateObjWithValidObject() {
        assertDoesNotThrow(() -> Validator.validateObj(new Object(), "Object cannot be null"),
            "Expected no exception for a valid object, but an exception was thrown.");
    }

    @Test
    void testValidateObjWithNullObject() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Validator.validateObj(null, "Object cannot be null"),
            "Expected IllegalArgumentException when passing null, but no exception was thrown.");
        assertEquals("Object cannot be null", exception.getMessage(),
            "Exception message did not match expected value.");
    }

    @Test
    void testValidateStringWithValidString() {
        assertDoesNotThrow(() -> Validator.validateString("valid", "String cannot be null", "String cannot be blank"),
            "Expected no exception for a valid string, but an exception was thrown.");
    }

    @Test
    void testValidateStringWithNullString() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Validator.validateString(null, "String cannot be null", "String cannot be blank"),
            "Expected IllegalArgumentException when passing null string, but no exception was thrown.");
        assertEquals("String cannot be null", exception.getMessage(),
            "Exception message did not match expected value.");
    }

    @Test
    void testValidateStringWithBlankString() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Validator.validateString(" ", "String cannot be null", "String cannot be blank"),
            "Expected IllegalArgumentException when passing a blank string, but no exception was thrown.");
        assertEquals("String cannot be blank", exception.getMessage(),
            "Exception message did not match expected value.");
    }

    @Test
    void testValidatePlayerIDWithValidID() {
        assertDoesNotThrow(() -> Validator.validatePlayerID(5),
            "Expected no exception for valid player ID (5), but an exception was thrown.");
    }

    @Test
    void testValidatePlayerIDWithNotStarted() {
        assertDoesNotThrow(() -> Validator.validatePlayerID(-1),
            "Expected no exception for NOT_STARTED (-1), but an exception was thrown.");
    }

    @Test
    void testValidatePlayerIDWithInvalidLowID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> Validator.validatePlayerID(0),
            "Expected InvalidPlayerIDException for player ID below the allowed range, but no exception was thrown.");
        assertEquals("Player ID should be between 1 and 9", exception.getMessage(),
            "Exception message did not match expected value.");
    }

    @Test
    void testValidatePlayerIDWithInvalidHighID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> Validator.validatePlayerID(10),
            "Expected InvalidPlayerIDException for player ID above the allowed range, but no exception was thrown.");
        assertEquals("Player ID should be between 1 and 9", exception.getMessage(),
            "Exception message did not match expected value.");
    }

    @Test
    void testValidateIntWithValidNumber() {
        assertDoesNotThrow(() -> Validator.validateInt(5, "Number should be non-negative"),
            "Expected no exception for a valid non-negative number (5), but an exception was thrown.");
    }

    @Test
    void testValidateIntWithZero() {
        assertDoesNotThrow(() -> Validator.validateInt(0, "Number should be non-negative"),
            "Expected no exception for zero (0), but an exception was thrown.");
    }

    @Test
    void testValidateIntWithNegativeNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Validator.validateInt(-1, "Number should be non-negative"),
            "Expected IllegalArgumentException for a negative number (-1), but no exception was thrown.");
        assertEquals("Number should be non-negative", exception.getMessage(),
            "Exception message did not match expected value.");
    }
}
