package uk.co.rodderscode.bbc;

import java.util.*;


public class Fetcher {

    final private String eol = System.getProperty("line.separator");

    public LinkedHashMap<String, String> getHttpHeaders(String response) {

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();

        int i = 0;
        for (String line : response.split(eol))
        {

            if (line.length() < 1)
                break;

            if (i == 0){
                headers.put("status", line);
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
}
