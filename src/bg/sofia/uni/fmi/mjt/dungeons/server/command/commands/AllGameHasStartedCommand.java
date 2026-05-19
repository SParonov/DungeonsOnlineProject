package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

public class AllGameHasStartedCommand extends Command {

    public AllGameHasStartedCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        ConcurrentHashMap<Integer, SocketChannel> clients = commandExecutor.connectedClients().getAllClients();
        for (SocketChannel client : clients.values()) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                System.lineSeparator() + "The game has started. Player 1's turn."));
        }
    }
}
