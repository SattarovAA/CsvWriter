package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;

@Data
@Builder
@AllArgsConstructor
public class Person {
    @CsvField(headerName = "First name")
    private String firstName;
    @CsvField()
    private String lastName;
    @CsvField(headerName = "")
    private int dayOfBirth;
    @CsvField(headerName = "Month Of Birth")
    private Months monthOfBirth;
    private int yearOfBirth;
}
