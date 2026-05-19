package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class GameOverCommand extends Command {
    private static final String STATUS_GAME_OVER = "game_over";

    public GameOverCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        writeClientOutput(clientChannel[ZERO], new MessageResponse(
            STATUS_GAME_OVER,
            System.lineSeparator() + "You have won the game!"));
    }
}
