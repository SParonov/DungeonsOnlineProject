package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class InvalidCommand extends Command {

    public InvalidCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    public void execute(SocketChannel... clientChannel) throws IOException {
        super.writeClientOutput(clientChannel[Command.ZERO], new InvalidCommandResponse(
            Command.STATUS_INVALID,
            "The command you have provided is invalid!"));
    }
}
