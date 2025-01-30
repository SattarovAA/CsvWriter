package org.writer.demo;

import org.writer.service.Writable;
import org.writer.service.impl.WriterService;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.demo.util.DataGenerator;

import java.io.File;
import java.util.List;

public class Demo {
    private static final String OUTPUT_DIRECTORY = "output";
    private static final String PERSONS_FILE = "persons.csv";
    private static final String STUDENTS_FILE = "students.csv";
    private final Writable writerService;

    public Demo() {
        writerService = new WriterService();
    }

    public void run() {
        writePersonsToFile();
        writeStudentsToFile();
    }

    /**
     * Записывает данные о людях в CSV-файл.
     * <br>
     * Генерирует 10 объектов {@link Person}.
     * Записывает данные в файл {@link #PERSONS_FILE}.
     */
    private void writePersonsToFile() {
        List<Person> persons = DataGenerator.generatePersons(10);
        String personsFilePath = OUTPUT_DIRECTORY
                .concat(File.separator)
                .concat(PERSONS_FILE);
        writerService.writeToFile(persons, personsFilePath);
    }


    /**
     * Записывает данные о студентах в CSV-файл.
     * <br>
     * Генерирует 5 объектов {@link Student}.
     * Записывает данные в файл {@link #STUDENTS_FILE}.
     */
    private void writeStudentsToFile() {
        List<Student> students = DataGenerator.generateStudents(5);
        String studentsFilePath = OUTPUT_DIRECTORY
                .concat(File.separator)
                .concat(STUDENTS_FILE);
        writerService.writeToFile(students, studentsFilePath);
    }
}
