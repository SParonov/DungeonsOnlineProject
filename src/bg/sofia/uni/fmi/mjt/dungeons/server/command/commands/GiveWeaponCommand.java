package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.Backpack;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Dagger;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Mace;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Sword;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.weapon.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Location;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Map;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class GiveWeaponCommand extends Command {
    private static final String REGEX = "\\d+/\\d+";
    private static final String SLASH = "/";
    private static final int SECOND_PARAM = 1;

    public GiveWeaponCommand(CommandExecutor commandExecutor,
                             String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String weaponType = params.getFirst();
        int weaponLevel = Integer.parseInt(params.get(SECOND_PARAM));
        int playerID = Integer.parseInt(params.getLast());

        Map map = commandExecutor.map();
        Location location = map.findPlayerLocation(playerID);

        if (!validateLocation(clientChannel[Command.ZERO], map, location)) {
            return;
        }

        switch (weaponType) {
            case DAGGER_NAME -> handleGiveWeapon(clientChannel[Command.ZERO], playerID, map,
                location, Dagger.NAME, weaponLevel);
            case SWORD_NAME -> handleGiveWeapon(clientChannel[Command.ZERO], playerID, map,
                location, Sword.NAME, weaponLevel);
            case MACE_NAME -> handleGiveWeapon(clientChannel[Command.ZERO], playerID, map,
                location, Mace.NAME, weaponLevel);
            default -> new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }

    private void handleGiveWeapon(SocketChannel client, int playerID, Map map,
                                  Location location, String weaponType, int weaponLevel) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(map, "Map should not be null");
        Validator.validateObj(location, "Location should not be null");

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);

        if (!hasWeapon(client, player, weaponType, weaponLevel)) return;

        int otherPlayerID = findOtherPlayerID(map, location, playerID);
        Player otherPlayer = commandExecutor.remainingPlayers().getPlayer(otherPlayerID);
        SocketChannel otherClient = commandExecutor
            .connectedClients()
            .getClient(otherPlayerID);

        if (!canReceiveWeapon(client, otherPlayer, otherPlayerID)) return;

        transferWeapon(client, otherClient, playerID, otherPlayer, weaponType, weaponLevel, otherPlayerID);
    }

    private boolean hasWeapon(SocketChannel client, Player player,
                              String weaponType, int weaponLevel) throws IOException {
        Validator.validateObj(player, "Player should not be null");

        List<Weapon> weapons = filterWeaponsByTypeAndLevel(player.getBackpack(), weaponType, weaponLevel);

        if (weapons.isEmpty()) {
            writeClientOutput(client, new MessageResponse(
                Command.STATUS_MESSAGE,
                "You don't have a " + weaponType.toLowerCase() + " of level " + weaponLevel + "!"
            ));
            return false;
        }
        return true;
    }

    private int findOtherPlayerID(Map map, Location location, int playerID) {
        String targetCell = map.getMap().get(location.i()).get(location.j());
        String[] parts = targetCell.split(SLASH);

        return (Integer.parseInt(parts[Command.ZERO]) == playerID)
            ? Integer.parseInt(parts[1])
            : Integer.parseInt(parts[0]);
    }

    private boolean canReceiveWeapon(SocketChannel client, Player otherPlayer, int otherPlayerID) throws IOException {
        Validator.validateObj(otherPlayer, "Other player should not be null");
        Validator.validatePlayerID(otherPlayerID);

        if (otherPlayer.getBackpack().isFull()) {
            writeClientOutput(client, new MessageResponse(
                Command.STATUS_MESSAGE,
                "Player " + otherPlayerID + "'s backpack is full!"
            ));
            return false;
        }
        return true;
    }

    private void transferWeapon(SocketChannel client, SocketChannel otherClient, int playerID,
                                Player otherPlayer, String weaponType,
                                int weaponLevel, int otherPlayerID) throws IOException {
        Validator.validateObj(otherClient, "Other client should not be null");

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Weapon weapon = filterWeaponsByTypeAndLevel(player.getBackpack(), weaponType, weaponLevel).getFirst();
        player.getBackpack().removeWeapon(weapon);
        otherPlayer.getBackpack().addWeapon(weapon);

        writeClientOutput(client, new MessageResponse(
            Command.STATUS_MESSAGE,
            weaponType + " (Level " + weaponLevel + ") successfully given to Player " + otherPlayerID
        ));

        writeClientOutput(otherClient, new MessageResponse(
            Command.STATUS_MESSAGE,
            "You received a " + weaponType.toLowerCase() + " (Level " + weaponLevel + ") from Player " + playerID
        ));
    }

    private List<Weapon> filterWeaponsByTypeAndLevel(Backpack backpack, String weaponType, int weaponLevel) {
        Validator.validateObj(backpack, "Backpack should not be null");

        return backpack.getWeapons()
            .stream()
            .filter(weapon -> weapon.getName().equalsIgnoreCase(weaponType) && weapon.level() == weaponLevel)
            .toList();
    }

    private boolean validateLocation(SocketChannel client, Map map, Location location) throws IOException {
        String targetCell = map.getMap().get(location.i()).get(location.j());

        if (!targetCell.matches(REGEX)) {
            writeClientOutput(client, new InvalidCommandResponse(
                Command.STATUS_INVALID,
                "You should be on a tile with a player!"
            ));
            return false;
        }

        return true;
    }
}
