package org.writer.demo.util;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Util-класс для генерации тестовых данных.
 */
public class DataGenerator {
    private static final Faker faker = new Faker();

    /**
     * Генерирует список объектов {@link Person} с указанным количеством элементов.
     *
     * @param count количество объектов {@link Person} для генерации.
     * @return список сгенерированных объектов {@link Person}.
     */
    public static List<Person> generatePersons(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2000))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Генерирует список объектов {@link Student} с указанным количеством элементов.
     *
     * @param count количество объектов {@link Student} для генерации.
     * @return список сгенерированных объектов {@link Student}.
     */
    public static List<Student> generateStudents(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Student.builder()
                        .name(faker.name().fullName())
                        .score(IntStream.range(0, 5)
                                .mapToObj(j -> String.valueOf(faker.number().numberBetween(1, 100)))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
