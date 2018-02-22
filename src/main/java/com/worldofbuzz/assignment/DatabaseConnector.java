package com.worldofbuzz.assignment;

import java.sql.*;
import java.util.Properties;

import com.mysql.jdbc.Driver;

public class DatabaseConnector {

    public static Properties p;

    public static void getLatest() {

        String getSKU = "SELECT SKU FROM sku_data";

        try {

            Statement stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(getSKU);
            rs.next();
            System.out.println("First SKU in the table: "+rs.getString("SKU"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Establish connection to MySQL
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://"+p.getProperty("dbaddress")+":3306/"+p.getProperty("database"),
                p.getProperty("dbuser"),
                p.getProperty("dbpw"));
    }
}
