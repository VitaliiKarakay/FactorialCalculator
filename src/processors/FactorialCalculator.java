package processors;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FactorialCalculator implements NumberProcessor {
    private final ExecutorService executorService;
    private final ResultProcessor resultProcessor;
    private final int maxNumbersPerSecond;

    public FactorialCalculator(int maxNumbersPerSecond, ResultProcessor resultProcessor) {
        this.executorService = Executors.newFixedThreadPool(maxNumbersPerSecond);
        this.resultProcessor = resultProcessor;
        this.maxNumbersPerSecond = maxNumbersPerSecond;
    }

    @Override
    public void processNumber(String number) {
        if (number.equals("END")) {
            executorService.shutdown();
            return;
        }

        CompletableFuture.supplyAsync(() -> calculateFactorial(number), executorService)
                .thenAccept(factorial -> {
                    String result = number.trim() + " = " + factorial;
                    resultProcessor.processResult(result);
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        try {
            int delay = 1000 / maxNumbersPerSecond;
            Thread.sleep(delay);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
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
