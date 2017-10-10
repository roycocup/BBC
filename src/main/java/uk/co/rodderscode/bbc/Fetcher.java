package uk.co.rodderscode.bbc;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public String getBody(String response) {

        String[] splitResponse = response.split(eol+eol);
        if (splitResponse.length > 1)
            return splitResponse[1];


        return "";
    }
}
