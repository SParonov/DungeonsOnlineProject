package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.Backpack;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
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

public class GivePotionCommand extends Command {
    private static final String SLASH = "/";
    private static final String REGEX = "\\d+/\\d+";
    private static final int ONE = 1;

    public GivePotionCommand(CommandExecutor commandExecutor,
                             String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String potionType = params.getFirst();
        int playerID = Integer.parseInt(params.getLast());

        Map map = commandExecutor.map();
        Location location = map.findPlayerLocation(playerID);

        if (!validateLocation(clientChannel[ZERO], map, location)) {
            return;
        }

        switch (potionType) {
            case HEALTH_NAME -> handleGivePotion(clientChannel[ZERO], playerID, map,
                location, HealthPotion.NAME, HEALTH_NAME_UPPER_LETT);
            case MANA_NAME -> handleGivePotion(clientChannel[ZERO], playerID, map,
                location, ManaPotion.NAME, MANA_NAME_UPPER_LETT);
            default -> new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }

    private void handleGivePotion(SocketChannel client, int playerID, Map map,
                                  Location location, String potionType, String potionName) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(map, "Map should not be null");
        Validator.validateObj(location, "Location should not be null");

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);

        if (!hasPotion(client, player, potionType, potionName)) return;

        int otherPlayerID = findOtherPlayerID(map, location, playerID);
        Player otherPlayer = commandExecutor.remainingPlayers().getPlayer(otherPlayerID);
        SocketChannel otherClient = commandExecutor.connectedClients().getClient(otherPlayerID);

        if (!canReceivePotion(client, otherPlayer, otherPlayerID)) return;

        transferPotion(client, otherClient, playerID, otherPlayer, potionType, potionName, otherPlayerID);
    }

    private boolean hasPotion(SocketChannel client, Player player,
                              String potionType, String potionName) throws IOException {
        Validator.validateObj(player, "Player should not be null");

        List<Potion> potions = filterPotionsByType(player.getBackpack(), potionType);

        if (potions.isEmpty()) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                "You don't have any " + potionName.toLowerCase() + " potions!"
            ));
            return false;
        }
        return true;
    }

    private int findOtherPlayerID(Map map, Location location, int playerID) {
        String targetCell = map.getMap().get(location.i()).get(location.j());
        String[] parts = targetCell.split(SLASH);

        return (Integer.parseInt(parts[ZERO]) == playerID)
            ? Integer.parseInt(parts[ONE])
            : Integer.parseInt(parts[ZERO]);
    }

    private boolean canReceivePotion(SocketChannel client, Player otherPlayer, int otherPlayerID) throws IOException {
        Validator.validateObj(otherPlayer, "Other player should not be null");

        if (otherPlayer.getBackpack().isFull()) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                "Player " + otherPlayerID + "'s backpack is full!"
            ));
            return false;
        }
        return true;
    }

    private void transferPotion(SocketChannel client, SocketChannel otherClient, int playerID,
                                Player otherPlayer, String potionType,
                                String potionName, int otherPlayerID) throws IOException {
        Validator.validateObj(otherClient, "Other client should not be null");
        Validator.validatePlayerID(otherPlayerID);

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Potion potion = filterPotionsByType(player.getBackpack(), potionType).getFirst();
        player.getBackpack().removePotion(potion);
        otherPlayer.getBackpack().addPotion(potion);

        writeClientOutput(client, new MessageResponse(
            STATUS_MESSAGE,
            potionName + " potion successfully given to Player " + otherPlayerID
        ));

        if (otherClient != null) {
            writeClientOutput(otherClient, new MessageResponse(
                STATUS_MESSAGE,
                "You received a " + potionName.toLowerCase() + " potion from Player " + playerID
            ));
        }
    }

    private boolean validateLocation(SocketChannel client, Map map, Location location) throws IOException {
        String targetCell = map.getMap().get(location.i()).get(location.j());

        if (!targetCell.matches(REGEX)) {
            writeClientOutput(client, new InvalidCommandResponse(
                STATUS_INVALID,
                "You should be on a tile with a player!"
            ));
            return false;
        }

        return true;
    }

    private List<Potion> filterPotionsByType(Backpack backpack, String potionName) {
        Validator.validateObj(backpack, "Backpack should not be null");

        return backpack.getPotions()
            .stream()
            .filter(potion -> potion.getName().equals(potionName))
            .toList();
    }
}
