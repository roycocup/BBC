package uk.co.rodderscode.bbc;


import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONWriter;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fetcher {

    public static void clear()
    {
        final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
    }

    public static void main(String[] args)
    {
        Fetcher f = new Fetcher();
    }

    /**********************
     *  Proof of concept  *
     *********************/

    public void poc()
    {
        // clear the screen
        clear();


        // start scanner loop
        final Scanner input = new Scanner( System.in );
        System.out.println("Enter urls followed by newline and a single dot (.) on an empty line to end.");

        ArrayList<String> lines = new ArrayList<>();
        String line = "";

        //Todo: make this match exclusively to a empty dot
        while(!line.equals(".")){
            line = input.next();
            lines.add(line);
        }


        // validate urls
        final ArrayList<String> validUrls = new ArrayList<>();
        final ArrayList<String> invalidUrls = new ArrayList<>();
        for(String url : lines){
            if(validateUrl(url)){
                validUrls.add(url);
            } else {
                invalidUrls.add(url);
            }
        }


//        System.out.println("These are valid");
//        for(String validUrl : validUrls)
//            System.out.println(validUrl);
//
//
//        System.out.println();
//        System.out.println("These are NOT valid");
//        for(String invalidUrl : invalidUrls)
//            System.out.println(invalidUrl);


        // fetch urls, store response, response time, content length, date, error if not reachable,
        LinkedList<HashMap> rawData = new LinkedList<>();
        for(String url : validUrls)
        {
            rawData.add(pocFetchUrlInfo(url));
        }

        StringBuilder sitesInfoJson = new StringBuilder();
        JSONWriter jsonWriter = new JSONWriter(sitesInfoJson).object();


        jsonWriter.key("data").value(rawData);

        jsonWriter.endObject();


        // collect stats
        HashMap<String, Integer> stats = new HashMap<>();
        for(HashMap site : rawData)
        {

            // it may be one of the invalid sites
            if (site.containsKey("Status_code"))
            {
                String code = (String) site.get("Status_code");
                if (stats.containsKey(code)){
                    int counter = stats.get(code);
                    stats.put(code, ++counter);
                } else
                    stats.put(code,  1);
            }

        }


        StringBuilder translatedVars = new StringBuilder();
        JSONWriter jsonwriter2 = new JSONWriter(translatedVars).object();


        ArrayList finalstats = new ArrayList();
        Iterator it = stats.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            HashMap<String, Integer> m = new HashMap<>();
            m.put("Status_code", Integer.parseInt(pair.getKey().toString()));
            m.put("Number_of_responses", Integer.parseInt(pair.getValue().toString()));

            finalstats.add(m);
            it.remove(); // avoids a ConcurrentModificationException
        }

        jsonwriter2.key("Stats").value(finalstats);
        jsonwriter2.endObject();


        System.out.println(sitesInfoJson);
        System.out.println(translatedVars);



        // todo: polish
        // todo: remove initial keys from json
        // todo: how to deploy gradle

    }

    /**
     * The program should make an http GET request to each valid address in its input and record particular properties of the http response in the program output.
     • The properties of interest are status code, content length and date-time of the response. These are normally available in the http response headers.
     • Output is a stream of JSON format documents that provide information about the http response for each address in the input
     * The program should identify and report invalid URLs, e.g. those that don't start with http:// or https://, or contain characters not allowed in a URL.
     • The program should cope gracefully when it makes a request to a slow or non-responsive web server, e.g. it could time out the request after ten seconds.
     • The program should have a good set of unit tests.
     • It must be possible to perform a test run, consisting of all unit tests, without accessing the Internet.
     * @param sUrl
     * @return
     */
    public static HashMap pocFetchUrlInfo(String sUrl)
    {
        // todo: timeout after x seconds
        HashMap<String, String> data = new HashMap();
        data.put("Url", sUrl);
        try {
            URLConnection conn = new URL(sUrl).openConnection();
            conn.getInputStream();
            Map<String, List<String>> header = conn.getHeaderFields();

            if (header.containsKey("Content-Length"))
                data.put("Content_length", header.get("Content-Length").toString());

            if (header.containsKey(null)){
                List<String> l = header.get(null);
                Pattern p = Pattern.compile("([\\d]{3})");
                Matcher m = p.matcher(l.toString());
                data.put("Status_code", (m.find())? m.group(1) : "");
            }

            data.put("Date", header.get("Date").toString());

        } catch (Exception e) {
            //todo: is the url not valid because not valid protocol
            //Matcher urlMatcher = URL_PATTERN.matcher(value);
//            if (!urlMatcher.matches()) {
//                return false;
//            }
            //todo: specify malformed, illegal etc...
            data.put("error", e.getClass().toString());
        }

        return data;
    }

    public static boolean validateUrl(String url) {
        String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);

        return urlValidator.isValid(url);
    }





}