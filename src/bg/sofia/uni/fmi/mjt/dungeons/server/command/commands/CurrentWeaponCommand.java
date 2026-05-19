package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.response.CurrentWeaponResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class CurrentWeaponCommand extends Command {
    private static final String STATUS_CURRENT_WEAPON = "current_weapon";

    public CurrentWeaponCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());
        Weapon weapon = commandExecutor.remainingPlayers().getPlayer(playerID).getCurrWeapon();

        writeClientOutput(clientChannel[ZERO], new CurrentWeaponResponse(
            STATUS_CURRENT_WEAPON,
            weapon.getName(),
            weapon.level()
        ));
    }
}
