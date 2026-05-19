package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NotYourTurnCommand extends Command {
    private static final String STATUS_NOT_YOUR_TURN = "not_your_turn";
    public NotYourTurnCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        writeClientOutput(clientChannel[Command.ZERO], new InvalidCommandResponse(
            STATUS_NOT_YOUR_TURN, "It's not your turn! Wait for player " + params.getFirst() + " to finish."));
    }
}
