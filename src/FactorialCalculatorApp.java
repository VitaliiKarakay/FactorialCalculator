import processors.FactorialCalculator;
import processors.FileWriterThread;
import processors.NumberProcessor;
import processors.ResultProcessor;

public class FactorialCalculatorApp {
    public static void main(String[] args) {
        final int maxThreads = 100;
        final String INPUT = "input.txt";
        final String OUTPUT = "output.txt";

        ResultProcessor resultProcessor = new FileWriterThread(OUTPUT);
        NumberProcessor factorialCalculator = new FactorialCalculator(maxThreads,  resultProcessor);
        Thread readerThread = new Thread(new FileReaderThread(INPUT, factorialCalculator));

        readerThread.start();
    }
}
