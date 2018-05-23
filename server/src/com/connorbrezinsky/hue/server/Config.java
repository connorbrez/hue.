package com.connorbrezinsky.hue.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by connorbrezinsky on 2017-06-03.
 */

public class Config {

    final Object workingDirectory = System.getProperty("user.dir");


   public static String getProperty(String p){
       String property = "";
       String path = "./config.properties";
       FileInputStream fis = null;

       try {
           Properties file = new Properties();
           String fileName = "config.properties";

           fis = new FileInputStream(path);


           if (fis != null) {
               file.load(fis);
           } else {
               throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
           }

           property = file.getProperty(p);

       } catch (Exception e) {
           System.out.println("Exception: " + e);
       } finally {
          try {
            if(fis!=null)fis.close();
          }catch (IOException e ){
              e.printStackTrace();
          }
       }
       return property;

   }

}
