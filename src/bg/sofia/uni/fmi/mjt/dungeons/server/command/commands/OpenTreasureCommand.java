package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.other.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.BackpackImpl;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Location;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Map;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Random;

public class OpenTreasureCommand extends Command {
    private static final String TREASURE = "T";
    private static final int ONE = 1;

    public OpenTreasureCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());
        Map map = commandExecutor.map();
        Location location = map.findPlayerLocation(playerID);

        if (!validateTreasurePickup(clientChannel[Command.ZERO], map, location)) {
            return;
        }

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Treasure treasure = map.updateMapRemoveTreasure(location);

        handleTreasurePickup(clientChannel[Command.ZERO], player, treasure);
    }

    private boolean validateTreasurePickup(SocketChannel client, Map map, Location location) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(map, "Map should not be null");
        Validator.validateObj(location, "Location should not be null");

        List<List<String>> stringsMap = map.getMap();

        if (!stringsMap.get(location.i()).get(location.j()).contains(TREASURE)) {
            writeClientOutput(client, new InvalidCommandResponse(
                Command.STATUS_INVALID,
                "You should be on a tile with a treasure!"
            ));
            return false;
        }

        Player player = commandExecutor.remainingPlayers().getPlayer(Integer.parseInt(params.getFirst()));
        if (player.getBackpack().isFull()) {
            writeClientOutput(client, new InvalidCommandResponse(
                Command.STATUS_INVALID,
                "Your backpack is full!"
            ));
            return false;
        }

        return true;
    }

    private void handleTreasurePickup(SocketChannel client, Player player, Treasure treasure) throws IOException {
        Validator.validateObj(player, "Player should not be null");
        Validator.validateObj(treasure, "Treasure should not be null");
        if (treasure.hasPotion() && treasure.hasWeapon() &&
            player.getBackpack().size() == BackpackImpl.CAPACITY - ONE) {

            if (new Random().nextBoolean()) {
                player.getBackpack().addPotion(treasure.getRandomPotion());
            } else {
                player.getBackpack().addWeapon(treasure.getRandomWeapon());
            }

            writeClientOutput(client, new MessageResponse(
                Command.STATUS_MESSAGE,
                "Your backpack is almost full, taking only one item from the treasure."
            ));
        } else {
            if (treasure.hasPotion()) {
                player.getBackpack().addPotion(treasure.getRandomPotion());
            }
            if (treasure.hasWeapon()) {
                player.getBackpack().addWeapon(treasure.getRandomWeapon());
            }

            writeClientOutput(client, new MessageResponse(
                Command.STATUS_MESSAGE,
                "Everything from the treasure was taken!"
            ));
        }
    }
}
