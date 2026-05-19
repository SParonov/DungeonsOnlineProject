package bg.sofia.uni.fmi.mjt.dungeons.client;

import bg.sofia.uni.fmi.mjt.dungeons.game.map.Map;
import bg.sofia.uni.fmi.mjt.dungeons.request.Request;
import bg.sofia.uni.fmi.mjt.dungeons.response.BackpackResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.ConnectedResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.CurrentWeaponResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.InvalidCommandResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.MapResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.MessageResponse;
import bg.sofia.uni.fmi.mjt.dungeons.response.Response;
import bg.sofia.uni.fmi.mjt.dungeons.response.StatsResponse;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DungeonsClient {

    private static final int SERVER_PORT = 7777;
    private static final String HOSTNAME = "localhost";
    private static boolean hasStarted;

    private static final int INITIAL_ID = -1;
    private static int playerID = INITIAL_ID;
    private static final int STATUS_ZERO = 0;
    private static final int STATUS_TWO = 2;
    private static final int STATUS_THREE = 3;

    private static final int DELAY = 500;

    private static final String COMMAND_CONNECT = "connect";
    private static final String STATUS_CONNECTED = "connected";
    private static final String STATUS_MESSAGE = "message";
    private static final String STATUS_GAME_OVER = "game_over";
    private static final String STATUS_DEAD = "dead";
    private static final String STATUS_MAP = "map";
    private static final String STATUS_BACKPACK = "backpack";
    private static final String STATUS_CURRENT_WEAPON = "current_weapon";
    private static final String STATUS_STATS = "stats";
    private static final String STATUS_LOBBY_FULL = "lobby_full";

    public static void main(String[] args) {
        DungeonsClient.start();
    }

    public static void start() {
        if (hasStarted) {
            return;
        }
        hasStarted = true;
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(HOSTNAME, SERVER_PORT));
            Gson gson = new Gson();
            System.out.println("Connected to server. Enter 'connect' to join the game if not full.");

            Thread serverResponseThread = createServerResponseThread(reader, gson);
            serverResponseThread.start();

            processUserInput(scanner, writer, gson);

        } catch (IOException e) {
            System.err.println("Network communication error" +
                "Try again later or contact administrator by providing the logs in <path_to_logs_file>");
            System.exit(STATUS_TWO);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted." +
                "Try again later or contact administrator by providing the logs in <path_to_logs_file>");
            System.exit(STATUS_THREE);
        }
    }

    private static Thread createServerResponseThread(BufferedReader reader, Gson gson) {
        Validator.validateObj(reader, "Reader should not be null");

        return new Thread(() -> {
            try {
                while (true) {
                    String jsonResponse = reader.readLine();
                    if (jsonResponse == null) break;

                    handleServerResponse(jsonResponse, gson);
                }
            } catch (IOException e) {
                System.err.println("Error reading from the server. " +
                    "Try again later or contact administrator by providing the logs in <logs.dat>");
                System.exit(STATUS_THREE);
            }
        });
    }

    private static void processUserInput(Scanner scanner, PrintWriter writer, Gson gson)
        throws InterruptedException {

        Validator.validateObj(scanner, "Scanner should not be null");
        Validator.validateObj(writer, "Writer should not be null");

        while (true) {
            Thread.sleep(DELAY);
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();

            if (command.equals(COMMAND_CONNECT) && playerID != INITIAL_ID) {
                System.out.println("You are already connected.");
                continue;
            }

            System.out.println("Sending command <" + command + "> to the server...");
            writer.println(gson.toJson(new Request(command, playerID)));
        }
    }

    private static void handleServerResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        Response response = gson.fromJson(jsonResponse, Response.class);
        String status = response.getStatus();

        switch (status) {
            case STATUS_CONNECTED -> handleConnectedResponse(jsonResponse, gson);
            case STATUS_MESSAGE, STATUS_GAME_OVER, STATUS_DEAD -> handleMessageResponse(jsonResponse, gson, status);
            case STATUS_MAP -> handleMapResponse(jsonResponse, gson);
            case STATUS_BACKPACK -> handleBackpackResponse(jsonResponse, gson);
            case STATUS_CURRENT_WEAPON -> handleCurrentWeaponResponse(jsonResponse, gson);
            case STATUS_STATS -> handleStatsResponse(jsonResponse, gson);
            case STATUS_LOBBY_FULL -> handleLobbyFullResponse();
            default -> handleInvalidCommandResponse(jsonResponse, gson);
        }
    }

    private static void handleConnectedResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        ConnectedResponse res = gson.fromJson(jsonResponse, ConnectedResponse.class);
        System.out.println(res.getMessage());
        playerID = res.getPlayerID();
    }

    private static void handleMessageResponse(String jsonResponse, Gson gson, String status) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");
        Validator.validateString(status,
            "Status should not be null",
            "Status should not be blank");

        MessageResponse res = gson.fromJson(jsonResponse, MessageResponse.class);
        System.out.println(res.getMessage());

        if (STATUS_GAME_OVER.equals(status) || STATUS_DEAD.equals(status)) {
            System.exit(STATUS_ZERO);
        }
    }

    private static void handleMapResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        MapResponse res = gson.fromJson(jsonResponse, MapResponse.class);
        Map.printMap(res.getMap());
    }

    private static void handleBackpackResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        BackpackResponse res = gson.fromJson(jsonResponse, BackpackResponse.class);
        System.out.println(res.getBackpackContent());
    }

    private static void handleCurrentWeaponResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        CurrentWeaponResponse res = gson.fromJson(jsonResponse, CurrentWeaponResponse.class);
        System.out.println("Current weapon:"
            + System.lineSeparator() + "NAME: " + res.getWeaponName()
            + System.lineSeparator() + "LEVEL: " + res.getWeaponLevel());
    }

    private static void handleStatsResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        StatsResponse res = gson.fromJson(jsonResponse, StatsResponse.class);
        res.getStats().printStats();
    }

    private static void handleLobbyFullResponse() {
        System.out.println("Lobby is full. Exiting...");
        System.exit(STATUS_ZERO);
    }

    private static void handleInvalidCommandResponse(String jsonResponse, Gson gson) {
        Validator.validateString(jsonResponse,
            "JSON response should not be null",
            "JSON response should not be blank");
        Validator.validateObj(gson, "Gson should not be null");

        InvalidCommandResponse res = gson.fromJson(jsonResponse, InvalidCommandResponse.class);
        System.out.println(res.getMessage());
    }

}
