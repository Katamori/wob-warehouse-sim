package com.worldofbuzz.assignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    public static Properties load(){
        Properties p = new Properties();
        InputStream is;

        try {
            is = new FileInputStream("general.properties");

            try{
                p.load(is);

            } catch (IOException ioe){

            } finally {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }

        } catch (FileNotFoundException fne){

        }

        return p;

    }
}
