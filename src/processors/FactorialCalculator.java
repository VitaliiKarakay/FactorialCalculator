package processors;

import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FactorialCalculator implements NumberProcessor {
    private final ExecutorService executorService;
    private final ResultProcessor resultProcessor;
    private final Semaphore throttlingSemaphore;
    private final AtomicInteger processedCount;
    private final ScheduledExecutorService scheduler;
    private volatile long startTime;
    private static final int MAX_OPERATIONS_PER_SECOND = 100;

    public FactorialCalculator(int maxThreads, ResultProcessor resultProcessor) {
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.resultProcessor = resultProcessor;
        this.throttlingSemaphore = new Semaphore(100);
        this.processedCount = new AtomicInteger(0);
        this.startTime = System.currentTimeMillis();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void processNumber(String number) {
        if (number.equals("END")) {
            executorService.shutdown();
            scheduler.shutdown();
            return;
        }

        checkAndUpdateThrottling();

        try {
            throttlingSemaphore.acquire();

            CompletableFuture.supplyAsync(() -> calculateFactorial(number), executorService)
                    .thenAccept(factorial -> {
                        String result = number.trim() + " = " + factorial;
                        resultProcessor.processResult(result);

                        throttlingSemaphore.release();
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void checkAndUpdateThrottling() {
        int currentProcessedCount = processedCount.incrementAndGet();
        long elapsedTime = System.currentTimeMillis() - startTime;
        long targetDelay = 1000L * currentProcessedCount / MAX_OPERATIONS_PER_SECOND;

        if (elapsedTime < targetDelay) {
            long remainingDelay = targetDelay - elapsedTime;
            scheduler.schedule(() -> {}, remainingDelay, TimeUnit.MILLISECONDS);
        }

        startTime = System.currentTimeMillis();
    }

    private BigInteger calculateFactorial(String number) {
        BigInteger factorial = BigInteger.ONE;
        int num = Integer.parseInt(number.trim());
        for (int i = 2; i <= num; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }


}
