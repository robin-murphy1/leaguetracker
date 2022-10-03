package com.span.devtest.services.data;

import com.span.devtest.exceptions.ProcessingException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDataCapturerTest {

    @Test
    public void givenATestFile_whenFileHasData_thenLinesAreReadIntoLst() {

        Path resource = Paths.get("src", "test", "resources", "data", "inputdata-1.txt");
        FileDataCapturer fileDataCapturer = new FileDataCapturer();

        List<String> teamResults = fileDataCapturer.captureData(resource.toAbsolutePath().toString());

        assertTrue(teamResults.size() == 5);
    }

    @Test
    public void givenATestFile_whenFileHasDuplicateLines_thenDuplicatesAreExcluded() {

        Path resource = Paths.get("src", "test", "resources", "data", "inputdata-duplicates.txt");
        FileDataCapturer fileDataCapturer = new FileDataCapturer();

        List<String> teamResults = fileDataCapturer.captureData(resource.toAbsolutePath().toString());

        assertTrue(teamResults.size() == 5);
    }

    @Test
    public void givenATestFile_whenFilePathIsValida_thenFileLoads() {

        Path resource = Paths.get("src", "test", "resources", "data", "inputdata-1.txt");
        FileDataCapturer fileDataCapturer = new FileDataCapturer();

        List<String> teamResults = fileDataCapturer.captureData(resource.toAbsolutePath().toString());

        assertNotNull(teamResults);
    }

    @Test
    public void givenATestFile_whenFilePathIsValid_thenExceptionExpected() {

        Path resource = Paths.get("src", "test", "resources", "inputdata-1.txt");
        FileDataCapturer fileDataCapturer = new FileDataCapturer();

        assertThrowsExactly(ProcessingException.class, () -> fileDataCapturer.captureData(resource.toAbsolutePath().toString()));
    }


}