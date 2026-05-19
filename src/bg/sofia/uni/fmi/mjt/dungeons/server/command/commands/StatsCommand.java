package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Stats;
import bg.sofia.uni.fmi.mjt.dungeons.response.StatsResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class StatsCommand extends Command {
    private static final String STATUS_STATS = "stats";

    public StatsCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());
        Stats stats = commandExecutor.remainingPlayers().getPlayer(playerID).getStats();

        writeClientOutput(clientChannel[Command.ZERO], new StatsResponse(
            STATUS_STATS,
            stats
        ));
    }
}
