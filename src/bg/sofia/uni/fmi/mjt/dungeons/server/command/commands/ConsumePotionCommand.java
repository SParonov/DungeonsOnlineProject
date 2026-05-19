package bg.sofia.uni.fmi.mjt.dungeons.server.command.commands;

import bg.sofia.uni.fmi.mjt.dungeons.game.entity.actor.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.Backpack;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.game.item.potion.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.Command;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class ConsumePotionCommand extends Command {
    public ConsumePotionCommand(CommandExecutor commandExecutor, String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String potionType = params.getFirst();
        int playerID = Integer.parseInt(params.getLast());
        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);
        Backpack backpack = player.getBackpack();

        switch (potionType) {
            case HEALTH_NAME -> handlePotionConsumption(clientChannel[ZERO],
                player, backpack, HealthPotion.NAME, HEALTH_NAME_UPPER_LETT);
            case MANA_NAME -> handlePotionConsumption(clientChannel[ZERO],
                player, backpack, ManaPotion.NAME, MANA_NAME_UPPER_LETT);
            default -> new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }

    private void handlePotionConsumption(SocketChannel client, Player player, Backpack backpack,
                                         String potionName, String potionType) throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(player, "Player should not be null");
        Validator.validateObj(backpack, "Backpack should not be null");

        List<Potion> potions = filterPotionsByType(backpack, potionName);

        if (potions.isEmpty()) {
            sendMessage(client, "You don't have any " + potionType.toLowerCase() + " potions!");
            return;
        }

        Potion potion = potions.getFirst();
        if (!player.usePotion(potion)) {
            sendMessage(client, "You don't have enough mana! " + potionType + " potion costs " + potion.getManaCost());
        } else {
            sendMessage(client, potionType + " potion consumed successfully!");
        }
    }

    private List<Potion> filterPotionsByType(Backpack backpack, String potionName) {
        return backpack.getPotions()
            .stream()
            .filter(potion -> potion.getName().equals(potionName))
            .toList();
    }

    private void sendMessage(SocketChannel client, String message) throws IOException {
        writeClientOutput(client, new MessageResponse(STATUS_MESSAGE, message));
    }
}
