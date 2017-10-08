package uk.co.rodderscode.bbc;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fetcher {


    public static void main(String[] args)
    {
        Fetcher fetcher = new Fetcher();
    }

    public ArrayList<String> getInput()
    {

        Scanner input = new Scanner( System.in );
        ArrayList<String> output = new ArrayList<>();

        while(input.hasNextLine()){
            String entry = input.nextLine();
            if (entry.matches("^(\\d).*?"))
            {
                continue;
            }
            if (entry.matches("^.+?(\\s)+.+?$"))
            {
                continue;
            }
            output.add(entry);
        }

        return output;
    }


}
