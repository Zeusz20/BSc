package com.zeusz.bsc.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class Cloud {

    private Cloud() { }

    public static String getCloudUrl(String path) {
        try(InputStream stream = Cloud.class.getClassLoader().getResourceAsStream("cloud.properties")) {
            Properties config = new Properties();
            config.load(stream);
            return config.get("cloud") + path;
        }
        catch(IOException e) {
            return path;
        }
    }

}
