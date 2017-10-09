import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestMain {

    private Fetcher fetcher;
    private String eol = System.getProperty("line.separator");

    @Before
    public void setup(){
        fetcher = new Fetcher();
    }

    @After
    public void tearDown(){}


    @Test
    public void mainClassCanBeCreated()
    {
        assertNotNull(fetcher);
    }


    @Test
    public void linesInInputAreNotValidUrls()
    {
        String[] invalidEntries = {
                "sdf://google.com",
                "http://sdf.sdf.sdf.sdf",
        };

        String entries = String.join(eol, invalidEntries);

        InputStream inputStream = new ByteArrayInputStream(entries.getBytes());

        System.setIn(inputStream);

        fetcher.processEntries();


        assert (fetcher.invalidUrls.contains(invalidEntries[0]));
        assert (fetcher.invalidUrls.contains(invalidEntries[1]));

    }


    @Test
    public void mainFieldContainsAllEntries()
    {
        String[] fakeEntries = {
                "sdf://google.com",
                "http://sdf.sdf.sdf.sdf",
                "http://google.com",
        };

        String entries = String.join(eol, fakeEntries);

        InputStream inputStream = new ByteArrayInputStream(entries.getBytes());

        System.setIn(inputStream);

        assertNull(fetcher.inputs);


        // run the loop
        fetcher.run();
        // exists?
        assertNotNull(fetcher.inputs);

        // is not empty?
        assert(0 < fetcher.inputs.size());

        int i = 0;
        for(String s : fetcher.inputs)
        {
            assertEquals (s, fakeEntries[i]);
            i++;
        }

    }

    @Test
    public void assertOnlyGoodUrlsPassValidation()
    {

        // bad ones
        assertFalse (fetcher.validateUrl("lksdjfsflk"));
        assertFalse (fetcher.validateUrl("htt://rodderscode.co.uk"));
        assertFalse (fetcher.validateUrl("http://rodderscode.c"));
        assertFalse (fetcher.validateUrl("http://rodderscode.java.cofres"));
        assertFalse (fetcher.validateUrl("htp://bbc.co.uk"));
        assertFalse (fetcher.validateUrl("ftp://bbc.co.uk"));

        //good ones
        assert (fetcher.validateUrl("http://yahoo.com"));
        assert (fetcher.validateUrl("http://bbc.co.uk"));
        assert (fetcher.validateUrl("https://bbc.co.uk"));
        assert (fetcher.validateUrl("http://bbc.co.uk/link/doesnt/exist"));
        assert (fetcher.validateUrl("http://rodderscode.co.uk"));
        assert (fetcher.validateUrl("http://www.rodderscode.co"));

    }


    @Test
    public void triageOfGoodAndBadUrlsIsSetToIndividualFields()
    {
        String[] fakeEntries = {
                //invalid ones
                "sdf://google.com",
                "http://sdf.sdf.sdf.sdf",
                "bad://address",
                // valid
                "http://google.com",
                "http://www.bbc.co.uk/iplayer",
                "https://google.com",
                "http://www.bbc.co.uk/missing/thing",
                "http://not.exists.bbc.co.uk/",
                "http://www.oracle.com/technetwork/java/javase/downloads/index.html",
                "https://www.pets4homes.co.uk/images/articles/1646/large/kitten-emergencies-signs-to-look-out-for- 537479947ec1c.jpg",
                "http://site.mockito.org/",

        };

        InputStream inputStream = new ByteArrayInputStream(String.join(eol, fakeEntries).getBytes());
        System.setIn(inputStream);

        // set the inputs
        fetcher.run();


        assert (fetcher.invalidUrls.contains(fakeEntries[0]));
        assert (fetcher.invalidUrls.contains(fakeEntries[1]));
        assert (fetcher.invalidUrls.contains(fakeEntries[2]));

//        assertFalse (fetcher.invalidUrls.contains(fakeEntries[0]));
//        assertFalse (fetcher.invalidUrls.contains(fakeEntries[1]));
//        assertFalse (fetcher.invalidUrls.contains(fakeEntries[2]));


    }


}
