package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTest {

    private Client client;
    private Server server;

    @BeforeEach
    public void setUp() {
        server = new Server();
        client = new Client(10);
    }

    @Test
    public void testRequestSending() {
        client.processRequests(server);

        assertTrue(client.listIsEmpty(), "Data list should be empty after processing");
        assertEquals(55, client.getAccumulator(), "Accumulator should match expected sum");
    }
}
