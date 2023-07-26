package processors;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FactorialCalculator implements NumberProcessor {
    private final int N;
    private final ExecutorService executorService;
    private final ResultProcessor resultProcessor;

    public FactorialCalculator(int n, ResultProcessor resultProcessor) {
        N = n;
        this.executorService = Executors.newFixedThreadPool(N);
        this.resultProcessor = resultProcessor;
    }

    @Override
    public void processNumber(int number) {
        if (number == -1) {
            executorService.shutdown();
            return;
        }

        CompletableFuture<BigInteger> futureFactorial = CompletableFuture.supplyAsync(() -> calculateFactorial(number), executorService);
        futureFactorial.thenAccept(factorial -> {
            String result = number + " = " + factorial.toString();
            resultProcessor.processResult(result);
        });
        try {
            Thread.sleep(1000 / N);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private BigInteger calculateFactorial(int number) {
        BigInteger factorial = BigInteger.ONE;
        for (int i = 2; i <= number; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }


}
