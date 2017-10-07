package uk.co.rodderscode.bbc;

import java.util.Scanner;

public class Fetcher {


    public static void main(String[] args)
    {
        Fetcher fetcher = new Fetcher();
    }

    public String getInput()
    {

        Scanner input = new Scanner( System.in );
        StringBuilder lines = new StringBuilder();

        while(input.hasNext()){
            lines.append(input.nextLine());
        }

        return lines.toString();
    }


}
