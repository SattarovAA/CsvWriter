package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {
    @CsvField(headerName = "Name")
    private String name;
    @CsvField(headerName = "Score")
    private List<String> score;
}