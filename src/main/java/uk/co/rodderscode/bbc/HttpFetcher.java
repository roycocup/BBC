package uk.co.rodderscode.bbc;

import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.HashedMap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class HttpFetcher {

    final private String eol = System.getProperty("line.separator");
    final public static String STATUSLINE = "status";


    public Map<String, List<String>> fetch(String url){
        try{
            URLConnection conn = new URL(url).openConnection();
            conn.getInputStream();
            return conn.getHeaderFields();
        } catch (MalformedURLException e){
            System.out.println(url + " is malformed. Ignoring.");
        } catch (IOException e){
            System.out.println("Unable to connect to " + url);
        }

        return null;
    }


    public String getHeadersLine(Map<String, List<String>> headers, String keyName) {
        String line = null;

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {

            String key = entry.getKey();

            // bypass status line
            if (key == null){
                if (keyName.equals(HttpFetcher.STATUSLINE))
                    line = entry.getValue().toString();
                continue;
            }

            if (key.equals(keyName)){
                line = entry.getValue().toString();
            }

        }

        if(line.length() > 0)
            line = line.replace("[", "").replace("]", "");

        return line;
    }
}
