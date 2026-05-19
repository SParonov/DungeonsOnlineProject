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

public class DiscardPotionCommand extends Command {
    public DiscardPotionCommand(CommandExecutor commandExecutor,
                                String... params) {
        super(commandExecutor, params);
    }

    @Override
    public void execute(SocketChannel... clientChannel) throws IOException {
        String potionType = params.getFirst();
        int playerID = Integer.parseInt(params.getLast());
        Player player = commandExecutor.remainingPlayers().getPlayer(playerID);

        switch (potionType) {
            case HEALTH_NAME -> handlePotionDiscard(clientChannel[ZERO],
                player, HealthPotion.NAME, HEALTH_NAME_UPPER_LETT);
            case MANA_NAME -> handlePotionDiscard(clientChannel[ZERO],
                player, ManaPotion.NAME, MANA_NAME_UPPER_LETT);
            default -> new InvalidCommand(commandExecutor).execute(clientChannel);
        }
    }

    void handlePotionDiscard(SocketChannel client, Player player, String potionName, String potionType)
        throws IOException {
        Validator.validateObj(client, "Client should not be null");
        Validator.validateObj(player, "Player should not be null");

        Backpack backpack = player.getBackpack();

        List<Potion> potions = filterPotionsByType(backpack, potionName);

        if (potions.isEmpty()) {
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                "You don't have any " + potionType.toLowerCase() + " potions!"
            ));
        } else {
            Potion potion = potions.getFirst();
            System.out.println(player.getBackpack().getPotions());
            player.getBackpack().removePotion(potion);
            writeClientOutput(client, new MessageResponse(
                STATUS_MESSAGE,
                potionType + " potion discarded successfully!"
            ));
        }
    }

    private List<Potion> filterPotionsByType(Backpack backpack, String potionName) {
        Validator.validateObj(backpack, "Backpack should not be null");

        return backpack.getPotions()
            .stream()
            .filter(potion -> potion.getName().equals(potionName))
            .toList();
    }
}
