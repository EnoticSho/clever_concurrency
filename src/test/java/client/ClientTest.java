package client;

import message.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ClientTest {

    private Client client;
    private Server mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = Mockito.mock(Server.class);
        client = new Client(10);
    }

    @Test
    public void testRequestSending() {
        // Given
        Mockito.when(mockServer.processRequest(any()))
                .thenReturn(new Response(1));

        // When
        client.processRequests(mockServer);

        // Then
        assertAll(() -> assertEquals(10, client.getAccumulator().get()),
                () -> assertTrue(client.listIsEmpty()));
    }
}
