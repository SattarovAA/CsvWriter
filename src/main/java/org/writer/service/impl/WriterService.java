package org.writer.service.impl;

import org.writer.service.Writable;
import org.writer.service.impl.csv.CsvWriter;

import java.util.List;

/**
 * Класс для записи информации в файл.
 */
public class WriterService implements Writable {
    /**
     * Использует {@link CsvWriter} для записи {@code data}
     * в файл с путем {@code fileName} в формате CSV.
     * <br>
     * Создает директорию для пути {@code fileName} если ее не существует.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        CsvWriter.create(data, fileName)
                .write();
    }
}
