import processors.FactorialCalculator;
import processors.FileWriterThread;
import processors.NumberProcessor;
import processors.ResultProcessor;

public class FactorialCalculatorApp {
    public static void main(String[] args) {
        final int threadsCount = 1000;
        final String INPUT = "input.txt";
        final String OUTPUT = "output.txt";

        ResultProcessor resultProcessor = new FileWriterThread(OUTPUT);
        NumberProcessor factorialCalculator = new FactorialCalculator(threadsCount,  resultProcessor);
        Thread readerThread = new Thread(new FileReaderThread(INPUT, factorialCalculator));

        readerThread.start();
    }
}
