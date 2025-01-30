package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей которые мы хотим включить в CSV файл.
 * Содержит {@link #headerName()} для переопределения
 * названия поля в заголовке CSV файла.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvField {
    String headerName() default "";
}
