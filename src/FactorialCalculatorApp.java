import processors.FactorialCalculator;
import processors.FileWriter;
import processors.NumberProcessor;
import processors.ResultProcessor;

public class FactorialCalculatorApp {
    public static void main(String[] args) {
        final int N = 5;
        final String INPUT = "input.txt";
        final String OUTPUT = "output.txt";

        ResultProcessor resultProcessor = new FileWriter(OUTPUT);
        NumberProcessor factorialCalculator = new FactorialCalculator(N,  resultProcessor);
        Thread readerThread = new Thread(new FileReaderThread(INPUT, factorialCalculator));

        readerThread.start();
    }
}