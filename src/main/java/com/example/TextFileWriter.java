package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter {
    private BufferedWriter bufferedWriter;

    TextFileWriter(String fileName, String textData) {

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName)));
            bufferedWriter.write(textData);
            System.out.printf("Transcript written to file: %s\n", fileName);
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("File not found: %s\n", fileName);
        } catch (IOException e) {
            System.out.printf("I/O error: %s\n", e.getMessage());
        }
    }
}
