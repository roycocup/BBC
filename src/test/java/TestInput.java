import org.junit.Test;
import uk.co.rodderscode.bbc.UrlStats;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestInput{


    /**
     *
     * We should expect an array of Strings each one with a new line terminated word.
     * If there is a space between 2 words the input should be ignored as is malformed.
     * Spaces can only exist before or after the word in which case they should be trimmed
     */

    @Test
    public void canSetStdIn()
    {
        String expected = "this-is-a-string";

        InputStream inputStream = new ByteArrayInputStream(expected.getBytes());

        System.setIn(inputStream);

        UrlStats urlStats = new UrlStats();
        ArrayList<String> inputText = urlStats.getInput();


        assertEquals(expected, inputText.toString().replace("[", "").replace("]", ""));

    }


    @Test
    public void inputReturnsSameArrayOfString()
    {

        // each one of these entries represents a batch of inputs from the user
        String[] listOfStrings = {"string-one", "string-two", "string-three"};


        StringBuilder fakeInput = new StringBuilder();

        // fake input should be a string of newline separated strings
        for(String line : listOfStrings){
            fakeInput.append(line);
            fakeInput.append("\\n");
        }

        InputStream inputStream = new ByteArrayInputStream(fakeInput.toString().getBytes());

        // setting the input stream with the entry
        System.setIn(inputStream);


        UrlStats urlStats = new UrlStats();

        ArrayList<String> output = urlStats.getInput();

        assertEquals ("["+fakeInput.toString()+"]", output.toString());


    }

    @Test
    public void inputShouldTrimWhiteSpaces()
    {
        // each one of these entries represents a batch of inputs from the user
        String[] validEntries = {
                "somestring",
                "http://anormal.url",
                "string-with-space-at-end ",
                " string-with-initial-space",
                "http://someurl\\nhttp://another.url\\n"
        };

        UrlStats urlStats = new UrlStats();

        // testing valid entries
        for(String expected : validEntries){
            InputStream inputStream = new ByteArrayInputStream(expected.getBytes());

            // setting the input stream with the entry
            System.setIn(inputStream);


            ArrayList<String> output = urlStats.getInput();

            assertEquals ("["+expected.toString().replace(" ", "")+"]", output.toString());
        }

    }

    @Test
    public void inputShouldIgnoreInvalidEntries()
    {
        String[] invalidEntries = {
                "string with spaces",
                "100string-starting-with-number",
        };

        UrlStats urlStats = new UrlStats();

        // testing invalid entries
        for(String entry : invalidEntries){

            InputStream inputStream = new ByteArrayInputStream(entry.getBytes());

            // setting the input stream with the entry
            System.setIn(inputStream);

            ArrayList<String> output = urlStats.getInput();

            assertEquals ("[]", output.toString());
        }
    }

    @Test
    public void fieldGetsPopulatedAfterMethodCall()
    {
        UrlStats urlStats = new UrlStats();
        urlStats.run();
        assertNotNull(urlStats.inputs);

    }

}
