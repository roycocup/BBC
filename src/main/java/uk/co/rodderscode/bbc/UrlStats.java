package uk.co.rodderscode.bbc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import org.json.JSONWriter;


public class UrlStats {

    // Where all user input will be stored
    public ArrayList<String> inputs;
    public ArrayList<HashMap<String, String>> finalStats = new ArrayList<>();

    public ArrayList<String> invalidUrls;
    public ArrayList<String> validUrls;
    private HttpFetcher httpFetcher;
    final static private String eol = System.getProperty("line.separator");


    public static void main(String[] args)
    {
        new UrlStats(new HttpFetcher()).run();
    }

    public UrlStats(HttpFetcher fetcher)
    {
        this.inputs         = new ArrayList<>();
        this.invalidUrls    = new ArrayList<>();
        this.validUrls      = new ArrayList<>();
        this.httpFetcher    = fetcher;
    }


    public void run()
    {
        clear();
        System.out.println("Enter urls followed by newline. Leave an empty line to end.");
        this.inputs = getInput();
        sortEntries(this.inputs);
        finalStats = collectInfo(this.validUrls);
        display(finalStats);
    }

    private void display(ArrayList<HashMap<String, String>> finalStats) {
        StringBuilder displayString = new StringBuilder();

        JSONWriter jsonWriter = new JSONWriter(displayString).object();


        jsonWriter.key("data").value(finalStats);

        jsonWriter.endObject();

        System.out.println(displayString.toString());

//        for (HashMap<String, String> site : finalStats)
//        {
//
//        }
    }

    // Clear the console screen
    private static void clear()
    {
        final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
    }

    public ArrayList<String> getInput()
    {
        ArrayList<String> output = new ArrayList<>();

        Scanner input = new Scanner( System.in );


        while(input.hasNextLine()){
            String entry = input.nextLine();

            if (entry.matches("^(\\d).*?"))
                continue;

            if (entry.matches("^.+?(\\s)+.+?$"))
                continue;

            entry = entry.replace(" ", "");
            output.add(entry);

            // end of line
            if (entry.length() == 0)
                break;

        }

        return output;
    }

    // Will split all entries as valid and invalid
    // relatively to the URL formation (URL Validator)
    public void sortEntries(ArrayList<String> entries) {

        for (String url : entries)
        {
            try{
                if (Validator.validateUrl(url))
                    this.validUrls.add(url);
                else
                    this.invalidUrls.add(url);    
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            
        }

    }


    public ArrayList<HashMap<String, String>> collectInfo(ArrayList<String> validUrls) {

        ArrayList<HashMap<String, String>> info = new ArrayList<>();
        HashMap<String, String> sitesInfo = new HashMap<>();

        for(String url : validUrls)
        {
            sitesInfo.put("url", url);
            try{
                Map<String, List<String>> headers = this.httpFetcher.fetch(url);

                String statusLine = this.httpFetcher.getHeadersLine(headers, HttpFetcher.STATUSLINE);
                sitesInfo.put(HttpFetcher.STATUSLINE, statusLine);

                String date = this.httpFetcher.getHeadersLine(headers, "Date");
                sitesInfo.put("Date", date);

                String length = this.httpFetcher.getHeadersLine(headers, "Content-Length");
                sitesInfo.put("Content-Length", length);
            } catch (MalformedURLException e) {
                sitesInfo.put("error", "Malformed URL");
            } catch (IOException e){
                sitesInfo.put("error", "Unable to contact url URL");
            }


            info.add(sitesInfo);
        }

        return info;
    }
}
