package com.worldofbuzz.assignment;

import java.sql.*;
import java.util.Properties;

public class DatabaseConnector {

    public static Properties p;

    public static ResultSet query(String string) throws SQLException {
        return connect().createStatement().executeQuery(string);
    }

    // Establish connection to MySQL
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://"+p.getProperty("dbaddress")+":3306/"+p.getProperty("database"),
                p.getProperty("dbuser"),
                p.getProperty("dbpw"));
    }
}
