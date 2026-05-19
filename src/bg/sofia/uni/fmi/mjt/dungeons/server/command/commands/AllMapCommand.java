package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.MapResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

public class AllMapCommand extends Command {
    private static final String STATUS_MAP = "map";

    public AllMapCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        ConcurrentHashMap<Integer, SocketChannel> clients = commandExecutor.connectedClients().getAllClients();
        for (SocketChannel client : clients.values()) {
            writeClientOutput(client, new MapResponse(
                STATUS_MAP,
                commandExecutor.map().getMap()
            ));
        }
    }
}
