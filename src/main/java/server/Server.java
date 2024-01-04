package server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import message.Request;
import message.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Server {

    @Getter
    private final List<Integer> serverData = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public Response processRequest(Request request) {
        log.debug("Processing request with value: {}", request.value());
        lock.lock();
        try {
            serverData.add(request.value());
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1001));
            log.debug("Added value to server data, current size: {}", serverData.size());
            return new Response(serverData.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
