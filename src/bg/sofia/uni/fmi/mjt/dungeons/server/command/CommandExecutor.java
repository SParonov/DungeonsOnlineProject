package bg.sofia.uni.fmi.mjt.dungeons.server.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.AllGameHasStartedCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.AllMapCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.AllPlayerTurnCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.BackpackCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.ChangeWeaponCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.ConnectCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.ConsumePotionCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.CurrentWeaponCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.DiscardPotionCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.DiscardWeaponCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.FightCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.GameOverCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.GetInfoCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.GivePotionCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.GiveWeaponCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.InvalidCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.LobbyFullCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.MovePlayerCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.NeededExpCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.NotYourTurnCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.OpenTreasureCommand;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.commands.StatsCommand;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Map;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.ConnectedClients;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.RemainingPlayers;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public record CommandExecutor(
    ByteBuffer buffer,
    RemainingPlayers remainingPlayers,
    ConnectedClients connectedClients,
    Map map) {

    private static final int STRINGS_LEN_THREE = 3;
    private static final int STRINGS_LEN_FOUR = 4;
    private static final int STRINGS_LEN_FIVE = 5;
    private static final int FIRST_PARAM = 1;
    private static final int SECOND_PARAM = 2;
    private static final int THIRD_PARAM = 3;
    private static final int FOURTH_PARAM = 4;
    private static final String UNDERSCORE_REGEX = "_";
    private static final String POSITIVE_INT_REGEX = "[1-9]";

    private static final String NOT_YOUR_TURN = "not_your_turn_";
    private static final String ALL_PLAYER_TURN = "all_player_turn_";
    private static final String MOVE = "move_";
    private static final String LOOK_BACKPACK = "look_backpack_";
    private static final String CURRENT_WEAPON = "current_weapon_";
    private static final String SEE_STATS = "see_stats_";
    private static final String OPEN_TREASURE = "open_treasure_";
    private static final String GET_INFO = "get_info_";
    private static final String CONSUME = "consume_";
    private static final String CHANGE = "change_";
    private static final String FIGHT = "fight_";
    private static final String NEEDED_EXP = "needed_exp_";
    private static final String DISCARD_POTION = "discard_potion_";
    private static final String DISCARD_WEAPON = "discard_weapon_";
    private static final String GIVE_POTION = "give_potion_";
    private static final String GIVE_WEAPON = "give_weapon_";
    private static final String ALL_MAP = "all_map";
    private static final String LOBBY_FULL = "lobby_full";
    private static final String CONNECT = "connect";
    private static final String ALL_GAME_STARTED = "all_game_has_started";
    private static final String GAME_OVER = "game_over";

    public void execute(String command, SocketChannel... clientChannel) throws IOException {
        Validator.validateObj(command, "Command should not be null");

        Validator.validateObj(clientChannel, "Client channel args should not be null");

        Command commandToExec = create(command);
        commandToExec.execute(clientChannel);
    }

    private Command create(String command) {
        return switch (command) {
            case String s when s.startsWith(NOT_YOUR_TURN) ->
                new NotYourTurnCommand(this, extractParam(s, NOT_YOUR_TURN));
            case String s when s.startsWith(ALL_PLAYER_TURN) ->
                new AllPlayerTurnCommand(this, extractParam(s, ALL_PLAYER_TURN));
            case String s when s.startsWith(MOVE) -> createMoveCommand(s);
            case String s when s.startsWith(LOOK_BACKPACK) ->
                new BackpackCommand(this, extractParam(s, LOOK_BACKPACK));
            case String s when s.startsWith(CURRENT_WEAPON) ->
                new CurrentWeaponCommand(this, extractParam(s, CURRENT_WEAPON));
            case String s when s.startsWith(SEE_STATS) ->
                new StatsCommand(this, extractParam(s, SEE_STATS));
            case String s when s.startsWith(OPEN_TREASURE) ->
                new OpenTreasureCommand(this, extractParam(s, OPEN_TREASURE));
            case String s when s.startsWith(GET_INFO) -> new GetInfoCommand(this, extractParam(s, GET_INFO));
            case String s when s.startsWith(CONSUME) -> createConsumeCommand(s);
            case String s when s.startsWith(CHANGE) -> createChangeWeaponCommand(s);
            case String s when s.startsWith(FIGHT) ->
                new FightCommand(this, extractParam(s, FIGHT));
            case String s when s.startsWith(NEEDED_EXP) -> new NeededExpCommand(this, extractParam(s, NEEDED_EXP));
            case String s when s.startsWith(DISCARD_POTION) -> createDiscardPotionCommand(s);
            case String s when s.startsWith(DISCARD_WEAPON) -> createDiscardWeaponCommand(s);
            case String s when s.startsWith(GIVE_POTION) -> createGivePotionCommand(s);
            case String s when s.startsWith(GIVE_WEAPON) -> createGiveWeaponCommand(s);
            default -> createSimpleCommand(command);
        };
    }

    private Command createMoveCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[SECOND_PARAM]);

        return (parts.length == STRINGS_LEN_THREE)
            ? new MovePlayerCommand(this, parts[FIRST_PARAM], parts[SECOND_PARAM])
            : new InvalidCommand(this);
    }

    private Command createConsumeCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[SECOND_PARAM]);

        return (parts.length == STRINGS_LEN_THREE)
            ? new ConsumePotionCommand(this, parts[FIRST_PARAM], parts[SECOND_PARAM])
            : new InvalidCommand(this);
    }

    private Command createChangeWeaponCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[THIRD_PARAM]);

        if (isNotPositiveInteger(parts[THIRD_PARAM])) {
            return new InvalidCommand(this);
        }

        return (parts.length == STRINGS_LEN_FOUR)
            ? new ChangeWeaponCommand(this, parts[FIRST_PARAM], parts[SECOND_PARAM], parts[THIRD_PARAM])
            : new InvalidCommand(this);
    }

    private Command createDiscardPotionCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[THIRD_PARAM]);

        return (parts.length == STRINGS_LEN_FOUR)
            ? new DiscardPotionCommand(this, parts[SECOND_PARAM], parts[THIRD_PARAM])
            : new InvalidCommand(this);
    }

    private Command createDiscardWeaponCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[FOURTH_PARAM]);

        if (isNotPositiveInteger(parts[THIRD_PARAM])) {
            return new InvalidCommand(this);
        }

        return (parts.length == STRINGS_LEN_FIVE)
            ? new DiscardWeaponCommand(this, parts[SECOND_PARAM], parts[THIRD_PARAM], parts[FOURTH_PARAM])
            : new InvalidCommand(this);
    }

    private Command createGivePotionCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[THIRD_PARAM]);

        return (parts.length == STRINGS_LEN_FOUR)
            ? new GivePotionCommand(this, parts[SECOND_PARAM], parts[THIRD_PARAM])
            : new InvalidCommand(this);
    }

    private Command createGiveWeaponCommand(String command) {
        String[] parts = command.split(UNDERSCORE_REGEX);

        checkIsNotValidPlayerID(parts[FOURTH_PARAM]);

        if (isNotPositiveInteger(parts[THIRD_PARAM])) {
            return new InvalidCommand(this);
        }

        return (parts.length == STRINGS_LEN_FIVE)
            ? new GiveWeaponCommand(this, parts[SECOND_PARAM], parts[THIRD_PARAM], parts[FOURTH_PARAM])
            : new InvalidCommand(this);
    }

    private Command createSimpleCommand(String command) {
        return switch (command) {
            case ALL_MAP -> new AllMapCommand(this);
            case LOBBY_FULL -> new LobbyFullCommand(this);
            case CONNECT -> new ConnectCommand(this);
            case ALL_GAME_STARTED -> new AllGameHasStartedCommand(this);
            case GAME_OVER -> new GameOverCommand(this);
            default -> new InvalidCommand(this);
        };
    }

    private String extractParam(String command, String prefix) {
        Validator.validateString(prefix,
            "Prefix should not be null",
            "Prefix should not be blank");

        return command.substring(prefix.length());
    }

    private static boolean isNotPositiveInteger(String str) {
        Validator.validateString(str,
            "String should not be null",
            "String should not be blank");

        return !str.matches(POSITIVE_INT_REGEX);
    }

    private static void checkIsNotValidPlayerID(String str) {
        Validator.validateString(str,
            "String should not be null",
            "String should not be blank");

        if (!str.matches(POSITIVE_INT_REGEX)) {
            throw new InvalidPlayerIDException("Player ID should be between 1 and 9");
        }
    }
}
