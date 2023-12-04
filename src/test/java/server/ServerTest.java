package server;

import message.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        IntStream.rangeClosed(1, 10)
                .parallel()
                .forEach(i -> {
                    try {
                        server.processRequest(new Request(i));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        assertTrue(server.validateData(10));
    }
}
