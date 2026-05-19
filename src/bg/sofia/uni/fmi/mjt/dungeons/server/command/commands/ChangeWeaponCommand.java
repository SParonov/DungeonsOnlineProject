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

public class ChangeWeaponCommand extends Command {
    private static final int SECOND_PARAM = 1;

    public ChangeWeaponCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String weaponType = params.getFirst();
        int weaponLevel = Integer.parseInt(params.get(SECOND_PARAM));
        int playerID = Integer.parseInt(params.getLast());

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Backpack backpack = player.getBackpack();

        switch (weaponType) {
            case DAGGER_NAME -> equipWeapon(clientChannel[ZERO], player, backpack, Dagger.NAME, weaponLevel);
            case SWORD_NAME -> equipWeapon(clientChannel[ZERO], player, backpack, Sword.NAME, weaponLevel);
            case MACE_NAME -> equipWeapon(clientChannel[ZERO], player, backpack, Mace.NAME, weaponLevel);
            default -> new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }

    private void equipWeapon(SocketChannel client, Player player, Backpack backpack,
                             String weaponName, int weaponLevel) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(player, "Player should not be null");
        Validator.validateObj(backpack, "Backpack should not be null");

        List<Weapon> weapons = filterWeaponsByTypeAndLevel(backpack, weaponName, weaponLevel);

        if (weapons.isEmpty()) {
            sendMessage(client, "You don't have a " + weaponName.toLowerCase() + " with level " + weaponLevel);
            return;
        }

        if (weaponLevel > player.getLevel()) {
            sendMessage(client, "You cannot equip this " + weaponName.toLowerCase() + " because your level is too low");
            return;
        }

        player.changeWeapon(weapons.getFirst());
        sendMessage(client, weaponName + " with level " + weaponLevel + " equipped successfully!");
    }

    private List<Weapon> filterWeaponsByTypeAndLevel(Backpack backpack, String weaponName, int weaponLevel) {
        return backpack.getWeapons()
            .stream()
            .filter(weapon -> weapon.getName().equals(weaponName))
            .filter(weapon -> weapon.level() == weaponLevel)
            .toList();
    }

    private void sendMessage(SocketChannel client, String message) throws IOException {
        writeClientOutput(client, new MessageResponse(STATUS_MESSAGE, message));
    }
}
