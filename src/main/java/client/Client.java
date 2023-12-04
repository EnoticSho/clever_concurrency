package client;

import message.Request;
import message.Response;
import server.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Client {
    private final List<Integer> dataList;
    private final ExecutorService executorService;
    private final AtomicInteger accumulator;

    public Client(int n) {
        dataList = Collections.synchronizedList(new ArrayList<>());
        IntStream.rangeClosed(1, n)
                .forEach(dataList::add);
        this.executorService = Executors.newCachedThreadPool();
        this.accumulator = new AtomicInteger(0);
    }

    public void processRequests(Server server) {
        try {
            while (!listIsEmpty()) {
                Future<Integer> future = sendRequest(server);
                Integer integer = future.get();
                if (integer != null) {
                    accumulator.addAndGet(integer);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            shutdown();
        }
    }

    private Future<Integer> sendRequest(Server server) {
        int index = ThreadLocalRandom.current().nextInt(dataList.size());
        int value = dataList.remove(index);

        return executorService.submit(() -> {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 501));
            Response response = server.processRequest(new Request(value));
            return response.getSize();
        });
    }

    private void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Integer getAccumulator() {
        return accumulator.get();
    }

    public boolean listIsEmpty() {
        return dataList.isEmpty();
    }
}
