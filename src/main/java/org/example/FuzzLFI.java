package org.example;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FuzzLFI {

    static final Path uploadDir = Paths.get("/root");
    
    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
        String jazzerInput = data.consumeString(15);

        try {
            ReadFileFromUploads(jazzerInput);
        } catch (IOException | InvalidPathException ignored) {
        }

    }


    /**
     * Demonstration of LFI vulnerability. An attacker can add double commas to read the files it's not supposed to
     *
     * @param fileName File which contents would you like to read. It will be appended to some dir
     * @return Option that contains a string representation of text file if it exists
     */
    static Optional<String> ReadFileFromUploads(String fileName) throws IOException {


        Path resultingPath = Paths.get(uploadDir.toString(), fileName);

        File file = new File(String.valueOf(resultingPath));

        // If file doesn't exist, return nothing
        if (!file.exists()) {
            return Optional.empty();
        }

        // Reading the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(resultingPath.toString()))) {
            StringBuilder sb = new StringBuilder();
            String line;

            // Read file line by line till the end
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            // Return the contents of file
            return Optional.of(sb.toString());
        }
    }
}
