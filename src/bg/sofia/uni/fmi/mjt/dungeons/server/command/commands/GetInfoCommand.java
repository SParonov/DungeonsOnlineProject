package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Location;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Map;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class GetInfoCommand extends Command {
    private static final String MINION = "M";
    private static final String SLASH = "/";
    private static final String REGEX = "\\d+/\\d+";
    private static final int ONE = 1;
    private static final String STATUS_DEAD = "dead";

    public GetInfoCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());
        Location location = commandExecutor.map().findPlayerLocation(playerID);
        Map map = commandExecutor.map();
        if (!validatePlayerLocation(clientChannel[ZERO], map, location)) {
            return;
        }
        if (map.getMinions().containsKey(location)) {
            handleMinionEncounter(clientChannel[ZERO], map, location, playerID);
        } else {
            handlePlayerEncounter(clientChannel[ZERO], map, location, playerID);
        }
    }

    private boolean validatePlayerLocation(SocketChannel client, Map map, Location location) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(map, "Map should not be null");
        Validator.validateObj(location, "Location should not be null");

        String targetCell = map.getMap().get(location.i()).get(location.j());

        if (!targetCell.matches(REGEX) && !targetCell.contains(MINION)) {
            writeClientOutput(client, new InvalidCommandResponse(
                STATUS_INVALID,
                "You should be on a tile with a player or a minion!"
            ));
            return false;
        }

        return true;
    }

    private void handleMinionEncounter(SocketChannel client, Map map, Location location, int playerID)
        throws IOException {

        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(map, "Map should not be null");
        Validator.validateObj(location, "Location should not be null");

        Minion minion = map.getMinions().get(location);
        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        int minionAttack = minion.attack();

        if (!player.receiveAttack(minionAttack)) {
            sendMinionAttackMessage(client, minion, player.getStats().getDefense());
        } else {
            sendDeathMessage(client, playerID);
        }
    }

    private void sendMinionAttackMessage(SocketChannel client, Minion minion, int playerDefense) throws IOException {
        Validator.validateObj(minion, "Minion should not be null");
        Validator.validateInt(playerDefense, "Player defense should not be negative");

        int minionAttack = minion.attack() - playerDefense;
        int actualAttack = Math.max(minionAttack, ZERO);

        writeClientOutput(client, new MessageResponse(
            STATUS_MESSAGE,
            "Minion level: " + minion.getLevel() + System.lineSeparator()
                + "Minion stats: " + System.lineSeparator()
                + "HEALTH: " + minion.getStats().getHealth() + System.lineSeparator()
                + "ATTACK: " + minion.getStats().getAttack() + System.lineSeparator()
                + "DEFENSE: " + minion.getStats().getDefense() + System.lineSeparator()
                + "You got too close to the minion and received " + Math.max(1, actualAttack) + " damage!"
                + System.lineSeparator()
        ));
    }

    private void sendDeathMessage(SocketChannel client, int playerID) throws IOException {
        writeClientOutput(client, new MessageResponse(
            STATUS_DEAD,
            "You got too close to the minion and it hit you pretty hard, you're dead"
        ));
        commandExecutor.map().updateMapRemovePlayer(playerID);
        commandExecutor.remainingPlayers().removePlayer(playerID);
        commandExecutor.connectedClients().removeClient(playerID);
    }

    private void handlePlayerEncounter(SocketChannel client, Map map, Location location, int playerID)
        throws IOException {

        String targetCell = map.getMap().get(location.i()).get(location.j());
        String[] parts = targetCell.split(SLASH);

        int otherPlayerID = (Integer.parseInt(parts[ZERO]) == playerID)
            ? Integer.parseInt(parts[ONE])
            : Integer.parseInt(parts[ZERO]);

        Player otherPlayer = commandExecutor.remainingPlayers().getPlayer(otherPlayerID);

        String message = "Player ID: " + otherPlayerID + System.lineSeparator()
            + "Level: " + otherPlayer.getLevel() + System.lineSeparator()
            + "Stats: " + System.lineSeparator()
            + "HEALTH: " + otherPlayer.getStats().getHealth() + System.lineSeparator()
            + "MANA: " + otherPlayer.getStats().getMana() + System.lineSeparator()
            + "ATTACK: " + otherPlayer.getStats().getAttack() + System.lineSeparator()
            + "DEFENSE: " + otherPlayer.getStats().getDefense() + System.lineSeparator()
            + "Current Weapon: " + (otherPlayer.getCurrWeapon().getName()
            + ", level " + otherPlayer.getCurrWeapon().level());

        writeClientOutput(client, new MessageResponse(STATUS_MESSAGE, message));
    }
}
