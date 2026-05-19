package bg.sofia.uni.fmi.mjt.dungeons.server.command;

import bg.sofia.uni.fmi.mjt.dungeons.response.Response;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
    protected static final String STATUS_MESSAGE = "message";
    protected static final String STATUS_INVALID = "invalid";
    protected static final int ZERO = 0;
    protected static final Gson GSON = new Gson();

    protected static final String DAGGER_NAME = "dagger";
    protected static final String SWORD_NAME = "sword";
    protected static final String MACE_NAME  = "mace";
    protected static final String HEALTH_NAME = "health";
    protected static final String MANA_NAME = "mana";
    protected static final String HEALTH_NAME_UPPER_LETT = "Health";
    protected static final String MANA_NAME_UPPER_LETT = "Mana";

    protected CommandExecutor commandExecutor;
    protected List<String> params;

    public Command(CommandExecutor commandExecutor, String... params) {
        Validator.validateObj(commandExecutor, "Command executor should not be null");
        Validator.validateObj(params, "Params should not be null");

        this.commandExecutor = commandExecutor;
        this.params = Arrays.asList(params);
    }

    public Command(CommandExecutor commandExecutor) {
        this(commandExecutor, "");
    }

    public abstract void execute(SocketChannel... clientChannel) throws IOException;

    protected void writeClientOutput(SocketChannel clientChannel, Response output)
        throws IOException {
        Validator.validateObj(clientChannel, "Client channel should not be null");
        Validator.validateObj(output, "Output should not be null");

        String json = GSON.toJson(output) + System.lineSeparator();
        byte[] jsonBytes = json.getBytes();

        commandExecutor.buffer().clear();
        commandExecutor.buffer().put(jsonBytes);
        commandExecutor.buffer().flip();

        clientChannel.write(commandExecutor.buffer());
    }
}
