package uk.co.rodderscode.bbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class HttpFetcher {

    final private String eol = System.getProperty("line.separator");
    final public static String STATUSLINE = "status";


    public Map<String, List<String>> fetch(String url)
            throws MalformedURLException, IOException, FileNotFoundException
    {

        URLConnection conn = new URL(url).openConnection();
        conn.getInputStream();
        return conn.getHeaderFields();
    }


    public String getHeadersLine(Map<String, List<String>> headers, String keyName){
        String line = null;


        // tou a procura de date mas o array ainda so da o null da statusline.
        // e vai pra exepcao

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            
            String key = entry.getKey();
            String value = entry.getValue().toString();

            // bypass status line
            if (key == null){
                if (keyName.equals(HttpFetcher.STATUSLINE))
                    line = value;
                continue;
            }

            if (key.equals(keyName)){
                line = value;
                continue;
            }

        }

        try{
            if(line.length() > 0)
                line = line.replace("[", "").replace("]", "");
        } catch (Exception e) {
//            System.out.println("Line is null on: " + keyName);
        }

        return line;

    }
}
