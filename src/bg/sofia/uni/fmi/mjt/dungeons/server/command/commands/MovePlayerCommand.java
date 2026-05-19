package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.DIRECTION;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MovePlayerCommand extends Command {
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String UP = "up";
    private static final String DOWN = "down";

    public MovePlayerCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String direction = params.getFirst();
        int playerID = Integer.parseInt(params.getLast());

        boolean invalidMoveCommand = false;
        boolean invalidCommand = false;
        switch (direction) {
            case LEFT -> invalidMoveCommand = !commandExecutor.map().updateMapMovePlayer(playerID, DIRECTION.LEFT);
            case RIGHT -> invalidMoveCommand = !commandExecutor.map().updateMapMovePlayer(playerID, DIRECTION.RIGHT);
            case UP -> invalidMoveCommand = !commandExecutor.map().updateMapMovePlayer(playerID, DIRECTION.UP);
            case DOWN -> invalidMoveCommand = !commandExecutor.map().updateMapMovePlayer(playerID, DIRECTION.DOWN);
            default -> invalidCommand = true;
        }
        if (invalidMoveCommand) {
            writeClientOutput(clientChannel[Command.ZERO], new InvalidCommandResponse(
                Command.STATUS_INVALID,
                "The move you made is invalid!"));
        } else if (invalidCommand) {
            new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }
}