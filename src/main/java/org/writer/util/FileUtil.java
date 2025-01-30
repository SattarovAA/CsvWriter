package org.writer.util;

import org.writer.exception.DirectoryCreationException;

import java.io.File;

/**
 * Класс для работы с файловой системой.
 */
public class FileUtil {

    /**
     * Создает директорию, если она не существует.
     *
     * @param directory Путь к директории.
     * @throws DirectoryCreationException если создание директории завершилось неудачно.
     */
    public static void createDirectoryIfNotExists(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new DirectoryCreationException(
                        "Failed to create directory: " + directory
                );
            }
        }
    }
}
