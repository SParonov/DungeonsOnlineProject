package bg.sofia.uni.fmi.mjt.dungeons.server.storage;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

public interface ConnectedClients {
    void addClient(int playerID, SocketChannel client);

    void removeClient(int playerID);

    SocketChannel getClient(int playerID);

    SocketChannel getRemainingChannel();

    ConcurrentHashMap<Integer, SocketChannel> getAllClients();
}
