package com.worldofbuzz.assignment;

import java.io.IOException;
import java.util.Properties;

public class Start {
    public static void main(String[] args) {
        Properties props = PropertyLoader.load();

        DatabaseConnector.p = props;
        DatabaseConnector.getLatest();

        try {
            CSVCreator.main();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}



