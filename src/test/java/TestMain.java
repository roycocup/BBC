import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestMain {

    private Fetcher fetcher;
    private String eol = System.getProperty("line.separator");
    private PrintStream originalOut;
    private ByteArrayOutputStream collectedOut;

    @Before
    public void setup(){
        // this will silence the output to Stdout
        originalOut = System.out;
        collectedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(collectedOut));

        //main object being tested
        fetcher = new Fetcher();
    }

    @After
    public void tearDown(){
        fetcher = null;
        System.setOut(originalOut);
    }

    public void insertAsInput(String[] arrayEntries)
    {
        String entries = String.join(eol, arrayEntries);

        InputStream inputStream = new ByteArrayInputStream(entries.getBytes());

        System.setIn(inputStream);
    }


    @Test
    public void mainClassCanBeCreated()
    {
        assertNotNull(fetcher);
    }

    @Test
    public void storingFieldsAreNullUntilInitIsCalled()
    {
        assertNotNull (fetcher.inputs);
        assertNotNull (fetcher.validUrls);
        assertNotNull (fetcher.invalidUrls);
    }


    @Test
    public void linesInInputAreNotValidUrls()
    {
        ArrayList<String> invalidEntries  = new ArrayList<>();
        invalidEntries.add("sdf://google.com");
        invalidEntries.add("http://sdf.sdf.sdf.sdf");

        fetcher.sortEntries(invalidEntries);

        assert (fetcher.invalidUrls.contains(invalidEntries.get(0)));
        assert (fetcher.invalidUrls.contains(invalidEntries.get(1)));

    }


    @Test
    public void mainFieldContainsAllEntries()
    {
        String[] fakeEntries = {
                "sdf://google.com",
                "http://sdf.sdf.sdf.sdf",
                "http://google.com",
        };

        insertAsInput(fakeEntries);

        assert(fetcher.inputs.size() < 1);


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

        insertAsInput(fakeEntries);

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
