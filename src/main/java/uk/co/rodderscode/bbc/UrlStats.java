package uk.co.rodderscode.bbc;


import java.util.*;


public class UrlStats {

    // Where all user input will be stored
    public ArrayList<String> inputs;
    final public ArrayList finalStats = new ArrayList();
    public HashMap<String, String> sitesInfo = new HashMap<>();

    public ArrayList<String> invalidUrls;
    public ArrayList<String> validUrls;
    private HttpFetcher httpFetcher;


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


    public void collectInfo(ArrayList<String> validUrls, HashMap<String, String> info) {

        for(String url : validUrls)
        {
            Map<String, List<String>> headers = this.httpFetcher.fetch(url);
            String value = this.httpFetcher.getHeadersLine(headers, HttpFetcher.STATUSLINE);
            info.put(HttpFetcher.STATUSLINE, value);
        }

    }
}
