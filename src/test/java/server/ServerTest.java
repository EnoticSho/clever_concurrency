package server;

import message.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ServerDataValidator;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerTest {
    private Server server;

    @BeforeEach
    public void setUp() {
        server = new Server();
    }

    @Test
    public void testRequestProcessing() {
        // Given & When
        IntStream.rangeClosed(1, 10)
                .parallel()
                .forEach(i -> server.processRequest(new Request(i)));

        // Then
        assertTrue(ServerDataValidator.validateData(server.getServerData(), 10));
    }
}
