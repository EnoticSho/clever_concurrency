import client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientServerIntegrationTest {

    private Server server;
    private Client client;

    @BeforeEach
    void setUp() {
        server = new Server();
        client = new Client(10);
    }

    @Test
    void testClientServerInteraction() {
        // Given
        int expectedAccumulator = 55;

        // When
        client.processRequests(server);

        //Then
        assertAll(() -> assertEquals(expectedAccumulator, client.getAccumulator()),
                () -> assertTrue(server.validateData(10)));
    }
}
