package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.response.ConnectedResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ConnectCommand extends Command {
    private static final String STATUS_CONNECTED = "connected";

    public ConnectCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = commandExecutor.remainingPlayers().addPlayer();
        commandExecutor.connectedClients().addClient(playerID, clientChannel[0]);

        writeClientOutput(clientChannel[ZERO], new ConnectedResponse(
            STATUS_CONNECTED,
            "Entered game, your ID is " + playerID,
            playerID));
    }
}
