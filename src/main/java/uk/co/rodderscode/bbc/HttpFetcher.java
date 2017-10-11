package uk.co.rodderscode.bbc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class HttpFetcher {

    final private String eol = System.getProperty("line.separator");
    final public static String STATUSLINE = "status";

    public LinkedHashMap<String, String> getHttpHeaders(String response) {

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();

        int i = 0;
        for (String line : response.split(eol))
        {

            if (line.length() < 1)
                break;

            if (i == 0){
                headers.put(STATUSLINE, line);
                i++;
                continue;
            }

            String[] keyvalue = line.split(":");
            headers.put(keyvalue[0], keyvalue[1]);
            i++;

        }

        return headers;
    }

    public String getBody(String response) {

        String[] splitResponse = response.split(eol+eol);
        if (splitResponse.length > 1)
            return splitResponse[1];

        return "";
    }

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
}
