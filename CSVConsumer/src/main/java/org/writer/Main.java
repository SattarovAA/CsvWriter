package org.writer;

import org.writer.model.Student;
import org.writer.service.Writable;
import org.writer.service.impl.WriterService;
import org.writer.service.impl.csv.CsvWriter;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Writable writable = new WriterService();
        givenCorrectDataWhenCreateThenCreateDir();
    }

    /**
     * Не стал сильно переделывать тест из CSVWriter-а.
     * Библиотека подключена и работает.
     */
    static void givenCorrectDataWhenCreateThenCreateDir() {
        List<Student> testList = List.of(
                Student.builder()
                        .name("test1")
                        .score(List.of("1", "2"))
                        .build(),
                Student.builder()
                        .name("test2")
                        .score(List.of("3", "2"))
                        .build()
        );
        String testOutputDirectory = "test\\output";
        String fileName = testOutputDirectory + File.separator + "output.csv";
        File dir = new File(testOutputDirectory);

        CsvWriter.create(testList, fileName).write();
    }
}