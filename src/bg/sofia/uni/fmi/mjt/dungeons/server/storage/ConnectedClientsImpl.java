package bg.sofia.uni.fmi.mjt.dungeons.server.storage;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidContainerAccessException;
import bg.sofia.uni.fmi.mjt.dungeons.validator.Validator;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectedClientsImpl implements ConnectedClients {
    private final ConcurrentHashMap<Integer, SocketChannel> clients;

    public ConnectedClientsImpl() {
        this.clients = new ConcurrentHashMap<>();
    }

    public void addClient(int playerID, SocketChannel client) {
        Validator.validatePlayerID(playerID);
        Validator.validateObj(client, "Client should not be null");

        clients.put(playerID, client);
    }

    public void removeClient(int playerID) {
        Validator.validatePlayerID(playerID);

        clients.remove(playerID);
    }

    @Override
    public SocketChannel getClient(int playerID) {
        Validator.validatePlayerID(playerID);

        return clients.get(playerID);
    }

    @Override
    public SocketChannel getRemainingChannel() {
        if (clients.size() != 1) {
            throw new InvalidContainerAccessException(
                "This method should be called when only there is only one channel left");
        }

        return clients.values().iterator().next();
    }

    public ConcurrentHashMap<Integer, SocketChannel> getAllClients() {
        return clients;
    }
}

