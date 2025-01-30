package org.writer.service.impl.csv;

import org.writer.annotation.CsvField;
import org.writer.exception.FileWritingException;
import org.writer.exception.IncorrectDataException;
import org.writer.exception.NoSuchAnnotatedFieldException;
import org.writer.exception.ValueAccessException;
import org.writer.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для записи информации в файл в формате CSV.
 */
public class CsvWriter {
    /**
     * Идентификатор для отсутствующих значений.
     */
    private static final String DASH = "-";
    /**
     * Разделитель между значениями полей.
     */
    private static final String DELIMITER = ",";
    /**
     * Разделитель между элементами коллекции.
     */
    private static final String ELEMENTS_DELIMITER = ";";
    /**
     * Идентификатор начала коллекции.
     */
    private static final String ELEMENTS_PREFIX = "[";
    /**
     * Идентификатор конца коллекции.
     */
    private static final String ELEMENTS_SUFFIX = "]";
    /**
     * Данные для записи.
     */
    private final List<?> data;
    /**
     * Путь файла для записи.
     */
    private final String fileName;
    /**
     * Поля для записи.
     */
    private final List<Field> fields;

    private CsvWriter(List<?> data, String fileName, List<Field> annotatedFields) {
        this.fields = annotatedFields;
        this.data = data;
        this.fileName = fileName;
    }

    /**
     * Создание объекта типа {@link CsvWriter}.
     *
     * @param data     данные для записи
     * @param fileName путь к файлу для записи
     * @return {@link CsvWriter} для записи данных в файл
     * @throws IncorrectDataException если {@code data} не содержит данных
     */
    public static CsvWriter create(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IncorrectDataException("Data list cannot be null or empty");
        }

        List<Field> annotatedFields = getAnnotatedFields(data.get(0));
        if (annotatedFields.isEmpty()) {
            throw new NoSuchAnnotatedFieldException("Data doesn't have fields with annotations.");
        }

        File filePatch = new File(fileName);
        FileUtil.createDirectoryIfNotExists(
                filePatch.getParentFile().getAbsolutePath()
        );

        return new CsvWriter(data, fileName, annotatedFields);
    }

    /**
     * Составляет список полей с аннотацией {@link CsvField}
     * для объекта {@code item}.
     * <br>
     * Использует функционал Reflection API.
     *
     * @param item объект для поиска полей
     * @return список полей объекта с аннотацией {@link CsvField}
     */
    private static List<Field> getAnnotatedFields(Object item) {
        return Arrays.stream(item.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvField.class))
                .collect(Collectors.toList());
    }

    /**
     * Использует {@link BufferedWriter} для записи {@link #data}
     * в файл с путем {@link #fileName} в формате CSV.
     *
     * @throws FileWritingException if IOException while writing to file
     */
    public void write() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writeLine(writer, getHeaders());
            for (Object item : data) {
                writeLine(writer, mapToCsvLine(item));
            }
        } catch (IOException e) {
            throw new FileWritingException("Error writing to file", e);
        }
    }

    /**
     * Записывает одну строку в файл.
     *
     * @param writer объект {@link BufferedWriter} для записи
     * @param line   строка, которую необходимо записать
     * @throws FileWritingException if IOException while writing to file
     */
    private void writeLine(BufferedWriter writer, String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new FileWritingException("Error writing line to file", e);
        }
    }

    /**
     * Возвращает строку с именами полей с {@link #DELIMITER} разделителем.
     * <br>
     * Имя берется из значения поля {@code headerName}
     * в аннотации {@link CsvField}.
     * Если {@code headerName} empty, используется имя поля.
     * <br>
     * Использует функционал Reflection API.
     *
     * @return строка заголовков с разделителями
     */
    private String getHeaders() {
        return fields.stream()
                .map(field -> {
                    CsvField annotation =
                            field.getAnnotation(CsvField.class);
                    return annotation.headerName().isEmpty()
                            ? field.getName()
                            : annotation.headerName();
                })
                .collect(Collectors.joining(DELIMITER));
    }

    /**
     * Преобразует данные объекта в строку CSV формата.
     * <br>
     * С помощью Reflection API мы получаем доступ ко всем полям объекта.
     * Заносим данные в строку с {@link #DELIMITER} разделителем.
     * Если же какое-либо значение у объекта не было получено,
     * то мы заменяем его на {@link #DASH}.
     * <br>
     * Если поле типа {@link List} используется {@link #mapListToCsvString(List)}.
     *
     * @param item объект для преобразования
     * @return строку значений полей объекта с разделителями
     * @see #mapListToCsvString(List)
     */
    private String mapToCsvLine(Object item) {
        StringBuilder line = new StringBuilder();
        for (Field field : fields) {
            Object value = getValue(item, field);

            if (!line.isEmpty()) {
                line.append(DELIMITER);
            }
            if (value == null) {
                line.append(DASH);
                continue;
            }
            if (isList(field)) {
                String result = mapListToCsvString((List<?>) value);
                line.append(result);
                continue;
            }
            line.append(value);
        }
        return line.toString();
    }

    /**
     * Возвращает значение поля {@code field} у объекта {@code item}.
     * <br>
     * Использует функционал Reflection API.
     *
     * @param item  объект содержащий поле {@code field}
     * @param field {@link Field} объекта для возврата значения
     * @return значение поля {@code field} у объекта {@code item}
     * @throws ValueAccessException если возникла проблема доступа к полю
     */
    private Object getValue(Object item, Field field) {
        field.setAccessible(true);
        try {
            return field.get(item);
        } catch (IllegalAccessException e) {
            throw new ValueAccessException(
                    "Failed to access to field value: " + e.getMessage()
            );
        }
    }

    /**
     * Преобразует {@link List} в строку.
     * Элементы разделены {@link #ELEMENTS_DELIMITER}.
     * Строка начинается с {@link #ELEMENTS_PREFIX}
     * и кончается {@link #ELEMENTS_SUFFIX}.
     *
     * @param list {@link List} для преобразования
     * @return строку элементов списка с разделителями
     */
    private String mapListToCsvString(List<?> list) {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining(
                        ELEMENTS_DELIMITER, ELEMENTS_PREFIX, ELEMENTS_SUFFIX)
                );
    }

    /**
     * Проверяет, является ли {@code field} объектом типа {@link List}.
     * <br>
     * Использует функционал Reflection API.
     *
     * @param field объект типа {@link Field} для проверки на тип
     * @return если {@code field} типа {@link List} - {@code true}.<br> Иначе - {@code false}
     */
    private boolean isList(Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType pType) {
                return pType.getRawType() == List.class;
            }
        }
        return false;
    }
}
