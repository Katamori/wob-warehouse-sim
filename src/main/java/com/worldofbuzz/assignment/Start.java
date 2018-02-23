package com.worldofbuzz.assignment;

import com.opencsv.CSVWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Start {

    private static Properties props;
    private static Date today = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    static {
        try {
            props = loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        String getSKU =
            "SELECT sku_data.*, sku_data.barcode, COUNT(sku_data.SKU)AS amount " +
            "FROM " +
            "(inventory INNER JOIN sku_data ON inventory.SKU = sku_data.SKU) " +
            "GROUP BY sku_data.SKU";

        try(
            CSVWriter writer = new CSVWriter(
                Files.newBufferedWriter(Paths.get("./report_"+dateFormat.format(today)+".csv")),
                '\t',
                '"',
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)
        ) {

            ResultSet rs = connect().createStatement().executeQuery(getSKU);

            //write csv header
            writer.writeNext(new String[]{"SKU", "Quantity", "SalePrice", "imageURL", "Barcode"});

            //write csv content
            while(!rs.isLast()){
                rs.next();

                String currentSKU = rs.getString("SKU");

                //SKU lines
                writer.writeNext(new String[]{
                    rs.getString("SKU"),
                    rs.getString("amount"),
                    calculateSalePrice(currentSKU, rs.getString("RetailPrice")),
                    "http://testimageUrl.exercise/"+currentSKU+".png",
                    rs.getString("barcode") });
            }

            System.out.println();

        } catch (IOException | SQLException | ParseException e) {
            e.printStackTrace();
        }
    }



    private static Properties loadProperties() throws IOException {

        Properties p = new Properties();
        p.load(new FileInputStream("general.properties"));

        return p;
    }



    private static Connection connect() throws SQLException {

        return DriverManager.getConnection(
        "jdbc:mysql://"+props.getProperty("dbaddress")+":3306/"+props.getProperty("database"),
            props.getProperty("dbuser"),
            props.getProperty("dbpw"));
    }



    private static String calculateSalePrice(String SKU, String price) throws SQLException, ParseException {

        ResultSet dates = connect().createStatement().executeQuery(
            "SELECT ItemReceivedDate " +
            "FROM inventory " +
            "WHERE SKU='"+SKU+"' " +
            "ORDER BY ItemReceivedDate " +
            "LIMIT 1"
        );

        dates.next();

        //get date from DB
        Date oldest = dateFormat.parse(dates.getString("ItemReceivedDate"));

        // Get diff in msec and "convert" to days
        long diff = (today.getTime() - oldest.getTime()) / (1000 * 60 * 60 * 24);

        System.out.println(today);
        System.out.println(oldest);
        System.out.println(diff);

        double multiplier;

        if(diff < 7){
            multiplier = 0.65;
        } else if (diff < 14){
            multiplier = 0.5;
        } else if (diff < 28){
            multiplier = 0.33;
        } else if (diff < 50){
            multiplier = 0.25;
        } else {
            multiplier = 0.195;
        }

        return String.valueOf(Double.parseDouble(price)*multiplier);

    }
}



