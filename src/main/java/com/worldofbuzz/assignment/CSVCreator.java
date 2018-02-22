package com.worldofbuzz.assignment;

import com.opencsv.*;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class CSVCreator {
    public static void main() throws IOException {
        try (

                CSVWriter csvWriter = new CSVWriter(
                        Files.newBufferedWriter(Paths.get("./sample.csv")),
                        '\t',
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ) {
            String[] headerRecord = {"SKU", "Quantity", "SalePrice", "imageURL"};
            csvWriter.writeNext(headerRecord);

            csvWriter.writeNext(new String[]{"Sundar Pichai ♥", "sundar.pichai@gmail.com", "+1-1111111111", "India"});
            csvWriter.writeNext(new String[]{"Satya Nadella", "satya.nadella@outlook.com", "+1-1111111112", "India"});
        }
    }
}