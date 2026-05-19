package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.Backpack;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Dagger;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Mace;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Sword;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DiscardWeaponCommand extends Command {
    private static final int SECOND_PARAM = 1;

    public DiscardWeaponCommand(CommandExecutor commandExecutor,
                                String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String weaponType = params.getFirst();
        int weaponLevel = Integer.parseInt(params.get(SECOND_PARAM));
        int playerID = Integer.parseInt(params.getLast());
        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);

        switch (weaponType) {
            case  DAGGER_NAME -> handleWeaponDiscard(clientChannel[ZERO], player, Dagger.NAME, weaponLevel);
            case SWORD_NAME -> handleWeaponDiscard(clientChannel[ZERO], player, Sword.NAME, weaponLevel);
            case MACE_NAME -> handleWeaponDiscard(clientChannel[ZERO], player, Mace.NAME, weaponLevel);
            default -> new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }

    private void handleWeaponDiscard(SocketChannel client, Player player, String weaponType, int weaponLevel)
        throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(player, "Player should not be null");

        Backpack backpack = player.getBackpack();

        List<Weapon> weapons = filterWeaponsByTypeAndLevel(backpack, weaponType, weaponLevel);
        if (weapons.isEmpty()) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                "You don't have a " + weaponType.toLowerCase() + " weapon of level " + weaponLevel + "!"
            ));
        } else {
            Weapon weapon = weapons.getFirst();
            backpack.removeWeapon(weapon);
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                weaponType + " weapon (Level " + weaponLevel + ") discarded successfully!"
            ));
        }
    }

    private List<Weapon> filterWeaponsByTypeAndLevel(Backpack backpack, String weaponType, int weaponLevel) {
        Validator.validateObj(backpack, "Backpack should not be null");

        return backpack.getWeapons()
            .stream()
            .filter(weapon -> weapon.getName().equals(weaponType) && weapon.level() == weaponLevel)
            .toList();
    }
}
