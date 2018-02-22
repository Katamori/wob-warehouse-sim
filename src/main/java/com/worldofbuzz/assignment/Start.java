package com.worldofbuzz.assignment;

import com.opencsv.CSVWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Start {
    public static void main(String[] args) {

        try(CSVWriter writer = new CSVWriter(
                Files.newBufferedWriter(Paths.get("./report.csv")),
                '\t',
                '"',
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)
        ) {

            String query =
                "SELECT sku_data.SKU, sku_data.barcode, COUNT(sku_data.SKU)AS amount \n" +
                    "from \n" +
                    "(inventory INNER JOIN sku_data ON inventory.SKU = sku_data.SKU)\n" +
                    "GROUP BY sku_data.SKU";


            Properties props = loadProperties();
            DatabaseConnector.p = props;
            ResultSet rs = DatabaseConnector.query(query);


            //write csv header
            writer.writeNext(new String[]{"SKU", "Quantity", "SalePrice", "imageURL", "Barcode"});

            //write csv content
            while(!rs.isLast()){
                rs.next();

                //SKU lines
                writer.writeNext(new String[]{
                        rs.getString("SKU"),
                        rs.getString("amount"),
                        "00,00",
                        "http://testimageUrl.exercise/"+rs.getString("SKU")+".png",
                        rs.getString("barcode") });

                System.out.println(rs.getString("SKU"));
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Properties loadProperties() throws IOException {

        Properties p = new Properties();
        p.load(new FileInputStream("general.properties"));

        return p;
    }
}



