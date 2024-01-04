package client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import message.Request;
import message.Response;
import server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class Client {
    private final List<Integer> dataList;
    private final ExecutorService executorService;

    @Getter
    private final AtomicInteger accumulator;

    public Client(int n) {
        dataList = new CopyOnWriteArrayList<>();
        IntStream.rangeClosed(1, n)
                .forEach(dataList::add);
        this.executorService = Executors.newFixedThreadPool(10);
        this.accumulator = new AtomicInteger();
    }

    public void processRequests(Server server) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        try {
            while (!listIsEmpty()) {
                CompletableFuture<Void> future = sendRequest(server)
                        .thenAccept(accumulator::getAndAdd)
                        .exceptionally(e -> {
                            log.error("Error processing request", e);
                            return null;
                        });
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            shutdown();
        }
    }

    private CompletableFuture<Integer> sendRequest(Server server) {
        int index = ThreadLocalRandom.current().nextInt(dataList.size());
        int value = dataList.remove(index);

        return CompletableFuture.supplyAsync(() -> {
            log.debug("Thread started for processing value: {}", value);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 501));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
            Response response = server.processRequest(new Request(value));
            log.debug("Thread completed processing for value: {}", value);
            return response.size();
        }, executorService);
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

    public boolean listIsEmpty() {
        return dataList.isEmpty();
    }
}
