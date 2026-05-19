package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NeededExpCommand extends Command {
    public NeededExpCommand(CommandExecutor commandExecutor,
                            String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());
        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);

        if (player.getLevel() == Actor.MAX_LEVEL) {
            writeClientOutput(clientChannel[Command.ZERO], new MessageResponse(
                Command.STATUS_MESSAGE,
                "You are max level!"
            ));
        } else {
            writeClientOutput(clientChannel[Command.ZERO], new MessageResponse(
                Command.STATUS_MESSAGE,
                "You need " + player.getExpForNextLevel() + " exp to level up."
            ));
        }
    }
}
