package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.BackpackImpl;
import bg.sofia.uni.fmi.mjt.dungeons.response.BackpackResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class BackpackCommand extends Command {
    private static final String STATUS_BACKPACK = "backpack";

    public BackpackCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());
        BackpackImpl backpack = commandExecutor.remainingPlayers().getPlayer(playerID).getBackpack();

        writeClientOutput(clientChannel[ZERO], new BackpackResponse(
            STATUS_BACKPACK,
            backpack.getPrintableBackpack()
        ));
    }
}
