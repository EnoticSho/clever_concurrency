package server;

import message.Request;
import message.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private final List<Integer> serverData = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public Response processRequest(Request request) throws InterruptedException {
        lock.lock();
        try {
            serverData.add(request.getValue());
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1001));
            return new Response(serverData.size());
        } finally {
            lock.unlock();
        }
    }

    public boolean validateData(int n) {
        if (serverData.size() != n) {
            return false;
        }

        for (int i = 1; i <= n; i++) {
            if (!serverData.contains(i)) {
                return false;
            }
        }
        return true;
    }
}
