package com.span.devtest.services.data;


import com.span.devtest.exceptions.ProcessingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDataCapturer implements DataCapturer {

    private List<String> leagueData;

    @Override
    public List<String> captureData(final String filepath) {

        Path file = Paths.get(filepath);

        try (Stream<String> lines = Files.lines(file)) {

            leagueData = lines
                    .map(String::trim).distinct()
                    .collect(Collectors.toList());

        } catch (IOException ioEX) {
            throw new ProcessingException("Error processing file: " + ioEX.getMessage());
        }

        return leagueData;
    }


}
