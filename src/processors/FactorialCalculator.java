package processors;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FactorialCalculator implements NumberProcessor {
    private final int threadsCount;
    private final ExecutorService executorService;
    private final ResultProcessor resultProcessor;

    public FactorialCalculator(int threadsCount, ResultProcessor resultProcessor) {
        this.threadsCount = threadsCount;
        this.executorService = Executors.newFixedThreadPool(this.threadsCount);
        this.resultProcessor = resultProcessor;
    }

    @Override
    public void processNumber(String number) {
        if (number.equals("END")) {
            executorService.shutdown();
            return;
        }

        CompletableFuture<BigInteger> futureFactorial = CompletableFuture.supplyAsync(() -> calculateFactorial(number), executorService);
        futureFactorial.thenAccept(factorial -> {
            String result = number.trim() + " = " + factorial.toString();
            resultProcessor.processResult(result);
        });
        try {
            Thread.sleep(1000 / threadsCount);
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
