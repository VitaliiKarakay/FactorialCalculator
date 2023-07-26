package processors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriterThread implements ResultProcessor {
    private final String filename;
    private boolean appendToFile = false;

    public FileWriterThread(String filename) {
        this.filename = filename;
    }

    @Override
    public void processResult(String result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, appendToFile))) {
            if (!appendToFile) {
                writer.print("");
                appendToFile = true;
            }
            writer.println(result);
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
