package bg.sofia.uni.fmi.mjt.dungeons.server.storage;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidContainerAccessException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.InvalidPlayerIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectedClientsImplTest {
    private ConnectedClientsImpl connectedClients;

    @Mock
    private SocketChannel mockChannel1;

    @Mock
    private SocketChannel mockChannel2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        connectedClients = new ConnectedClientsImpl();
    }

    @Test
    void testAddClientSuccessfully() {
        connectedClients.addClient(1, mockChannel1);

        assertEquals(mockChannel1, connectedClients.getClient(1),
            "Added client should be retrievable by its ID");
    }

    @Test
    void testAddClientWithNullSocketChannel() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> connectedClients.addClient(1, null));

        assertEquals("Client should not be null", exception.getMessage(),
            "Adding a null client should throw an IllegalArgumentException");
    }

    @Test
    void testAddClientWithInvalidPlayerID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> connectedClients.addClient(10, mockChannel1)); // ID out of range

        assertEquals("Player ID should be between 1 and 9", exception.getMessage(),
            "Adding a client with an invalid player ID should throw InvalidPlayerIDException");
    }

    @Test
    void testRemoveClientSuccessfully() {
        connectedClients.addClient(1, mockChannel1);
        connectedClients.removeClient(1);

        assertNull(connectedClients.getClient(1),
            "Removed client should no longer exist in the storage");
    }

    @Test
    void testRemoveClientWithInvalidPlayerID() {
        Exception exception = assertThrows(InvalidPlayerIDException.class,
            () -> connectedClients.removeClient(10)); // Invalid ID

        assertEquals("Player ID should be between 1 and 9", exception.getMessage(),
            "Removing a client with an invalid player ID should throw InvalidPlayerIDException");
    }

    @Test
    void testGetClientThatExists() {
        connectedClients.addClient(2, mockChannel2);

        assertEquals(mockChannel2, connectedClients.getClient(2),
            "getClient() should return the correct SocketChannel for a given player ID");
    }

    @Test
    void testGetClientThatDoesNotExist() {
        assertNull(connectedClients.getClient(3),
            "getClient() should return null for a player ID that does not exist");
    }

    @Test
    void testGetRemainingChannelWhenOnlyOneExists() {
        connectedClients.addClient(1, mockChannel1);

        assertEquals(mockChannel1, connectedClients.getRemainingChannel(),
            "getRemainingChannel() should return the only remaining SocketChannel");
    }

    @Test
    void testGetRemainingChannelWhenMultipleClientsExist() {
        connectedClients.addClient(1, mockChannel1);
        connectedClients.addClient(2, mockChannel2);

        Exception exception = assertThrows(InvalidContainerAccessException.class,
            () -> connectedClients.getRemainingChannel());

        assertEquals("This method should be called when only there is only one channel left",
            exception.getMessage(),
            "Calling getRemainingChannel() with multiple clients should throw InvalidContainerAccessException");
    }

    @Test
    void testGetAllClients() {
        connectedClients.addClient(1, mockChannel1);
        connectedClients.addClient(2, mockChannel2);

        ConcurrentHashMap<Integer, SocketChannel> allClients = connectedClients.getAllClients();

        assertEquals(2, allClients.size(), "getAllClients() should return all stored clients");
        assertTrue(allClients.containsKey(1), "getAllClients() should contain player ID 1");
        assertTrue(allClients.containsKey(2), "getAllClients() should contain player ID 2");
    }
}
