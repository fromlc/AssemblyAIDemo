package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextFileWriter {
    private FileOutputStream fileOS;

    TextFileWriter(String fileName, String textData) {

        try {
            fileOS = new FileOutputStream(new File(fileName));
            byte[] dataBytes = textData.getBytes();
            fileOS.write(dataBytes);
            fileOS.flush();
            System.out.printf("Transcript written to file: %s\n", fileName);
        } catch (FileNotFoundException e) {
            System.out.printf("File not found: %s\n", fileName);
        } catch (IOException e) {
            System.out.printf("I/O error: %s\n", e.getMessage());
        }
    }
}
