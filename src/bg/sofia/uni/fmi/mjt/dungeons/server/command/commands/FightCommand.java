package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
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
import java.util.Random;

public class FightCommand extends Command {
    private static final String MINION = "M";
    private static final String REGEX_SLASH = "/";
    private static final int ONE = 1;
    private static final String STATUS_DEAD = "dead";
    private static final String REGEX = "\\d+/\\d+";

    public FightCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        int playerID = Integer.parseInt(params.getFirst());

        Map map = commandExecutor.map();
        Location location = map.findPlayerLocation(playerID);

        if (!validateLocation(clientChannel[ZERO], map, location)) {
            return;
        }

        if (map.getMinions().containsKey(location)) {
            handleMinionFight(clientChannel[ZERO], map, location, playerID);
        } else {
            handlePlayerFight(clientChannel[ZERO], map, location, playerID);
        }
    }

    private boolean validateLocation(SocketChannel client, Map map, Location location) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(map, "Map should not be null");
        Validator.validateObj(location, "Location should not be null");

        List<List<String>> stringsMap = map.getMap();

        String targetCell = stringsMap.get(location.i()).get(location.j());
        if (!targetCell.contains(MINION) && !targetCell.matches(REGEX)) {
            writeClientOutput(client, new InvalidCommandResponse(
                STATUS_INVALID,
                "You should be on a tile with a minion or another player!"
            ));
            return false;
        }

        return true;
    }

    private void handleMinionFight(SocketChannel client, Map map, Location location, int playerID) throws IOException {
        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Minion minion = map.getMinions().get(location);

        boolean playerSurvives = handleFight(player, minion);

        if (playerSurvives) {
            handleWin(client, "You won the fight! You killed the minion!");
            int level = commandExecutor.map().updateMapRemoveMinion(location);
            handleExp(client, player, level);
        } else {
            commandExecutor.map().updateMapRemovePlayer(playerID);
            handleDeath(client, "You lost the fight! The minion killed you!", playerID);
        }
    }

    private void handlePlayerFight(SocketChannel client, Map map, Location location, int playerID) throws IOException {
        List<List<String>> stringsMap = map.getMap();
        String targetCell = stringsMap.get(location.i()).get(location.j());
        int otherPlayerID = getOtherPlayerID(targetCell, playerID);

        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Player otherPlayer = commandExecutor.remainingPlayers().getPlayer(otherPlayerID);
        SocketChannel otherClient = commandExecutor.connectedClients().getClient(otherPlayerID);

        boolean playerSurvives = handleFight(player, otherPlayer);

        if (playerSurvives) {
            handleWin(client, "You won the fight! You killed Player " + otherPlayerID + "!");
            int level = commandExecutor.map().updateMapRemovePlayer(otherPlayerID);
            handleExp(client, player, level);
            handleDeath(otherClient, "You lost the fight! Player " + playerID + " killed you!", otherPlayerID);
        } else {
            handleWin(otherClient, "You won the fight! You killed Player " + playerID + "!");
            int level = commandExecutor.map().updateMapRemovePlayer(playerID);
            handleExp(otherClient, otherPlayer, level);
            handleDeath(client, "You lost the fight! Player " + otherPlayerID + " killed you!", playerID);
        }
    }

    private int getOtherPlayerID(String targetCell, int playerID) {
        Validator.validateString(targetCell,
            "Target cell should not be null",
            "Target cell should not be blank");

        String[] parts = targetCell.split(REGEX_SLASH);
        return (Integer.parseInt(parts[ZERO]) == playerID)
            ? Integer.parseInt(parts[ONE])
            : Integer.parseInt(parts[ZERO]);
    }

    private void handleExp(SocketChannel client, Player player, int level) throws IOException {
        Validator.validateObj(player, "Player should not be null");
        Validator.validateInt(level, "Level should not be negative");

        int oldPlayerLevel = player.getLevel();
        boolean notMaxLevel = player.receiveExp(level);

        if (!notMaxLevel) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                "You can't receive any more exp, you are max level!"
            ));
        }
        if (oldPlayerLevel != player.getLevel()) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                "You leveled up! You are now at level " + player.getLevel()
            ));
        }
    }

    private void handleDeath(SocketChannel client, String message, int playerID) throws IOException {
        commandExecutor.remainingPlayers().removePlayer(playerID);
        commandExecutor.connectedClients().removeClient(playerID);
        writeClientOutput(client, new MessageResponse(
            STATUS_DEAD,
            message
        ));
    }

    private void handleWin(SocketChannel client, String message) throws IOException {
        writeClientOutput(client, new MessageResponse(
            STATUS_MESSAGE,
            message
        ));
    }

    private boolean handleFight(Player player, Actor enemy) {
        Validator.validateObj(enemy, "Enemy must not be null");

        Random random = new Random();
        boolean playerStarts = random.nextBoolean();

        while (true) {
            if (playerStarts) {
                if (performAttack(player, enemy)) return true;
                if (performAttack(enemy, player)) return false;
            } else {
                if (performAttack(enemy, player)) return false;
                if (performAttack(player, enemy)) return true;
            }
        }
    }

    private boolean performAttack(Actor attacker, Actor defender) {
        int attackDamage = attacker.attack();
        return defender.receiveAttack(attackDamage);
    }
}
