package uk.co.rodderscode.bbc;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Fetcher {

    public ArrayList<String> invalidUrls;
    public ArrayList<String> inputs;

    public static void main(String[] args)
    {
        new Fetcher().run();
    }

    public void run()
    {
        clear();
        System.out.println("Enter urls followed by newline. Leave an empty line to end.");
        this.inputs = getInput();
    }

    // Clear the console screen
    public static void clear()
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


    public void processEntries() {

        this.invalidUrls = getInput();
    }
}
