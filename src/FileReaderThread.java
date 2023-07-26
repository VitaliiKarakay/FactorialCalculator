import processors.NumberProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderThread implements Runnable{
    private final String filename;
    private final NumberProcessor numberProcessor;

    public FileReaderThread(String filename, NumberProcessor numberProcessor) {
        this.filename = filename;
        this.numberProcessor = numberProcessor;
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                numberProcessor.processNumber(line);
            }
        } catch (IOException | NumberFormatException exception ) {
            exception.printStackTrace();
        }
        numberProcessor.processNumber("END");
    }
}
