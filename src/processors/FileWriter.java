package processors;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter implements ResultProcessor {
    private final String filename;

    public FileWriter(String filename) {
        this.filename = filename;
    }

    @Override
    public void processResult(String result) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new java.io.FileWriter(filename))) {
            bufferedWriter.write(result);
            bufferedWriter.newLine();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
