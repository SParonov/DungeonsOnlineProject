package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class LobbyFullCommand extends Command {
    private static final String STATUS_LOBBY_FULL = "lobby_full";

    public LobbyFullCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        writeClientOutput(clientChannel[Command.ZERO], new InvalidCommandResponse(
            STATUS_LOBBY_FULL, "Lobby is already full"));
    }
}
