package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {


        if (args.length < 1) {
            System.err.println("Usage:  file_to_read_from_directory");
            return;
        }

        try {
            Optional<String> fileContent =  FuzzLFI.ReadFileFromUploads(args[0]);
            System.out.println(fileContent);
        } catch (FileNotFoundException e) {
            System.err.println("Can't find the file");
        } catch (IOException e) {
            System.err.println("An exception occurred while reading the file");
        }


//        FuzzException.fuzzMe("fuzzing fuz");
    }

}