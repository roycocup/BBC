package uk.co.rodderscode.bbc;


import java.util.ArrayList;
import java.util.Arrays;


public class Fetcher {

    final private String eol = System.getProperty("line.separator");

    public String getHttpHeaders(String response) {

        StringBuilder headers = new StringBuilder();

        for (String line : response.split(eol))
        {
            if(line.length() < 1)
                break;
            headers.append(line);
            headers.append(eol);
        }

        return headers.toString();
    }
}
