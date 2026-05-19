package bg.sofia.uni.fmi.mjt.dungeons.server;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPortException;
import bg.sofia.uni.fmi.mjt.dungeons.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.Map;
import bg.sofia.uni.fmi.mjt.dungeons.request.Request;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.ConnectedClients;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.ConnectedClientsImpl;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.RemainingPlayers;
import bg.sofia.uni.fmi.mjt.dungeons.server.storage.RemainingPlayersImpl;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DungeonsServer {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";

    private static final int NUM_OF_PLAYERS = 2;
    private static final int FIRST_PLAYER_ID = 1;

    private final RemainingPlayers remainingPlayers;
    private final ConnectedClients connectedClients;
    private final Map map;
    private CommandExecutor commandExecutor;
    private int currentTurnPlayerID = -1;

    private final Gson gson;

    private final int port;
    private boolean isServerWorking;
    private boolean gameHasStarted;

    private ByteBuffer buffer;
    private Selector selector;

    private static final String UNDERSCORE = "_";
    private static final String COMMAND_CONNECT = "connect";
    private static final String COMMAND_ALL_GAME_HAS_STARTED = "all_game_has_started";
    private static final String COMMAND_ALL_MAP = "all_map";
    private static final String COMMAND_INVALID = "invalid";
    private static final String COMMAND_LOBBY_FULL = "lobby_full";
    private static final String COMMAND_NOT_YOUR_TURN = "not_your_turn_";
    private static final String COMMAND_LOOK_BACKPACK = "look_backpack";
    private static final String COMMAND_SEE_STATS = "see_stats";
    private static final String COMMAND_CURRENT_WEAPON = "current_weapon";
    private static final String COMMAND_NEEDED_EXP = "needed_exp";
    private static final String COMMAND_ALL_PLAYER_TURN = "all_player_turn_";
    private static final String COMMAND_GAME_OVER = "game_over";
    private static final int PORT_MIN = 0;
    private static final int PORT_MAX = 65535;

    private static final Logger LOGGER = Logger.getLogger(DungeonsServer.class.getName());
    private static final String LOG_FILE = "logs.dat";

    static {
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        final int port = 7777;
        DungeonsServer server = new DungeonsServer(port);
        server.start();
    }

    public DungeonsServer(int port) {
        if (port < PORT_MIN || port > PORT_MAX) {
            throw new InvalidPortException("Port should be a valid port");
        }

        this.port = port;
        this.remainingPlayers = new RemainingPlayersImpl(NUM_OF_PLAYERS);
        this.connectedClients = new ConnectedClientsImpl();
        this.map = new Map(remainingPlayers);
        this.gson = new Gson();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            commandExecutor = new CommandExecutor(buffer, remainingPlayers, connectedClients, map);
            configureServerSocketChannel(serverSocketChannel, selector);
            isServerWorking = true;
            System.out.printf("Server opened. Started listening on port %d" + System.lineSeparator(), port);
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    handleKeys();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error processing client request", e);
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start server", e);
            throw new UncheckedIOException("Failed to start server", e);
        }
    }

    private void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void handleKeys() throws IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            SocketChannel clientChannel = null;
            try {
                if (key.isReadable()) {
                    clientChannel = (SocketChannel) key.channel();
                    Request clientRequest = getClientInput(clientChannel);
                    if (clientRequest == null) {
                        continue;
                    }
                    int playerID = clientRequest.playerID();

                    if (!gameHasStarted) {
                        handlePreGame(clientRequest, clientChannel);
                    } else {
                        handleInGame(clientRequest, clientChannel, playerID);
                    }
                } else if (key.isAcceptable()) {
                    accept(selector, key);
                }
            } catch (IOException e) {
                handleException(e, clientChannel);
            } finally {
                keyIterator.remove();
            }
        }
    }

    private int getPlayerIDFromChannel(SocketChannel channel) {
        for (java.util.Map.Entry<Integer, SocketChannel> entry : connectedClients.getAllClients().entrySet()) {
            if (entry.getValue().equals(channel)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private void handleException(IOException e, SocketChannel clientChannel) {
        int playerID = -1;
        if (clientChannel != null) {
            playerID = getPlayerIDFromChannel(clientChannel);
        }

        if (playerID != -1) {
            LOGGER.log(Level.WARNING, "Error for Player ID: " + playerID, e);
        } else {
            LOGGER.log(Level.WARNING, "Error handling client request", e);
        }
    }

    private void handlePreGame(Request clientRequest, SocketChannel clientChannel)
        throws IOException {

        Validator.validateObj(clientRequest, "Client request should not be null");
        Validator.validateObj(clientChannel, "Client channel should not be null");

        String command = clientRequest.command().trim();

        if (command.equals(COMMAND_CONNECT)) {
            commandExecutor.execute(command, clientChannel);

            if (remainingPlayers.isFull()) {
                System.out.println("The game has started");
                gameHasStarted = true;
                currentTurnPlayerID = FIRST_PLAYER_ID;

                commandExecutor.execute(COMMAND_ALL_GAME_HAS_STARTED);
                commandExecutor.execute(COMMAND_ALL_MAP);
            }
        } else {
            commandExecutor.execute(COMMAND_INVALID, clientChannel);
        }
    }

    private void handleInGame(Request clientRequest, SocketChannel clientChannel, int playerID)
        throws IOException {

        Validator.validateObj(clientRequest, "Client request should not be null");
        Validator.validateObj(clientChannel, "Client channel should not be null");
        Validator.validatePlayerID(playerID);

        if (clientRequest.command().trim().equals(COMMAND_CONNECT)) {
            commandExecutor.execute(COMMAND_LOBBY_FULL, clientChannel);
        } else if (playerID != currentTurnPlayerID) {
            commandExecutor.execute(COMMAND_NOT_YOUR_TURN + currentTurnPlayerID, clientChannel);
        } else {
            executePlayerCommand(clientRequest, clientChannel);
            if (!clientRequest.command().equals(COMMAND_LOOK_BACKPACK) &&
                !clientRequest.command().equals(COMMAND_SEE_STATS) &&
                !clientRequest.command().equals(COMMAND_CURRENT_WEAPON) &&
                !clientRequest.command().equals(COMMAND_NEEDED_EXP)) {
                commandExecutor.execute(COMMAND_ALL_MAP);
                switchTurn();
                commandExecutor.execute(COMMAND_ALL_PLAYER_TURN + currentTurnPlayerID);
            }
        }
    }

    private void executePlayerCommand(Request clientRequest, SocketChannel clientChannel)
        throws IOException {

        Validator.validateObj(clientRequest, "Client request should not be null");
        Validator.validateObj(clientChannel, "Client channel should not be null");

        String command = clientRequest.command() + UNDERSCORE + clientRequest.playerID();

        commandExecutor.execute(command, clientChannel);
    }

    private void switchTurn() throws IOException {
        if (remainingPlayers.hasOneLeft()) {
            announceWinner();
            return;
        }

        int nextTurn = currentTurnPlayerID;
        do {
            nextTurn = (nextTurn % NUM_OF_PLAYERS) + 1;
        } while (!remainingPlayers.hasPlayer(nextTurn));

        currentTurnPlayerID = nextTurn;
    }

    private void announceWinner() throws IOException {
        int winnerID = remainingPlayers.getRemainingPlayerID();
        SocketChannel remainingChannel = connectedClients.getRemainingChannel();

        System.out.println("Player " + winnerID + " has won the game!");

        commandExecutor.execute(COMMAND_GAME_OVER, remainingChannel);

        stop();
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector)
        throws IOException {

        Validator.validateObj(channel, "Channel should not be null");
        Validator.validateObj(selector, "Selector should not be null");

        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        Validator.validateObj(selector, "Selector should not be null");
        Validator.validateObj(key, "Key must not be null");

        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = sockChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private Request getClientInput(SocketChannel clientChannel) throws IOException {
        Validator.validateObj(clientChannel, "Client channel should not be null");

        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        String clientInput = new String(clientInputBytes, StandardCharsets.UTF_8);

        return gson.fromJson(clientInput, Request.class);
    }
}
