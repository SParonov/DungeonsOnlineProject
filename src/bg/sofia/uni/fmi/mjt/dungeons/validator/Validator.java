package bg.sofia.uni.fmi.mjt.dungeons.validator;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;

public class Validator {
    private static final int MIN_ID = 1;
    private static final int MAX_ID = 9;
    private static final int NOT_STARTED = -1;

    public static void validateObj(Object obj, String nullMess) {
        if (obj == null) {
            throw new IllegalArgumentException(nullMess);
        }
    }

    public static void validateString(String str, String nullMess, String blankMess) {
        validateObj(str, nullMess);

        if (str.isBlank()) {
            throw new IllegalArgumentException(blankMess);
        }
    }

    public static void validatePlayerID(int playerID) {
        if (playerID == NOT_STARTED) return;

        if (playerID < MIN_ID || playerID > MAX_ID) {
            throw new InvalidPlayerIDException("Player ID should be between 1 and 9");
        }
    }

    public static void validateInt(int num, String numMess) {
        if (num < 0) {
            throw new IllegalArgumentException(numMess);
        }
    }
}
