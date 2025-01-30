package org.writer.service.impl.csv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.writer.exception.IncorrectDataException;
import org.writer.exception.NoSuchAnnotatedFieldException;
import org.writer.model.Student;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CsvWriterTest Tests")
public class CsvWriterTest {
    private final String testPath = "test\\output.csv";
    private final List<Student> testList = List.of(
            Student.builder()
                    .name("test1")
                    .score(List.of("1", "2"))
                    .build(),
            Student.builder()
                    .name("test2")
                    .score(List.of("3", "2"))
                    .build()
    );

    @Test
    @DisplayName("Throw when creation with null data.")
    void givenNullDataWhenCreateThenThrow() {
        assertThrows(IncorrectDataException.class,
                () -> CsvWriter.create(null, testPath)
        );
    }

    @Test
    @DisplayName("Throw when creation with empty data.")
    void givenEmptyDataWhenCreateThenThrow() {
        assertThrows(IncorrectDataException.class,
                () -> CsvWriter.create(Collections.EMPTY_LIST, testPath)
        );
    }

    @Test
    @DisplayName("Throw when data without annotated fields.")
    void givenDataWithoutAnnotatedFieldsWhenCreateThenThrow() {
        assertThrows(NoSuchAnnotatedFieldException.class,
                () -> CsvWriter.create(List.of(1, 2), testPath)
        );
    }

    @Test
    @DisplayName("Create directory and file with correct name if not exist.")
    void givenCorrectDataWhenCreateThenCreateDir() {
        String testOutputDirectory = "test\\output";
        String fileName = testOutputDirectory + File.separator + "output.csv";
        File dir = new File(testOutputDirectory);

        CsvWriter.create(testList, fileName);

        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }

    @Test
    @DisplayName("create new CsvWriter with correct data.")
    void givenCorrectDataWhenCreateThenNewWriter() {
        assertDoesNotThrow(() -> CsvWriter.create(testList, testPath));
    }

    @Test
    @DisplayName("Correct number of line in result file.")
    void givenListWithHundredElementsWhenWriteTheOneHundredAndOneElements()
            throws IOException {
        CsvWriter csvWriter = CsvWriter.create(testList, testPath);
        Path filePath = new File(testPath).toPath();

        csvWriter.write();
        List<String> lines = Files.readAllLines(filePath);

        assertEquals(testList.size() + 1, lines.size());
    }
}
