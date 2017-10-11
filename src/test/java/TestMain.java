import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.UrlStats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class TestMain {

    private UrlStats urlStats;
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
        urlStats = new UrlStats();
    }

    @After
    public void tearDown(){
        urlStats = null;
        System.setOut(originalOut);
    }

    private void insertAsInput(String[] arrayEntries)
    {
        String entries = String.join(eol, arrayEntries);

        InputStream inputStream = new ByteArrayInputStream(entries.getBytes());

        System.setIn(inputStream);
    }


    @Test
    public void mainClassCanBeCreated()
    {
        assertNotNull(urlStats);
    }

    @Test
    public void storingFieldsAreNullUntilInitIsCalled()
    {
        assertNotNull (urlStats.inputs);
        assertNotNull (urlStats.validUrls);
        assertNotNull (urlStats.invalidUrls);
    }


    @Test
    public void linesInInputAreNotValidUrls()
    {
        ArrayList<String> invalidEntries  = new ArrayList<>();
        invalidEntries.add("sdf://google.com");
        invalidEntries.add("http://sdf.sdf.sdf.sdf");

        urlStats.sortEntries(invalidEntries);

        assert (urlStats.invalidUrls.contains(invalidEntries.get(0)));
        assert (urlStats.invalidUrls.contains(invalidEntries.get(1)));

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

        assert(urlStats.inputs.size() < 1);


        // run the loop
        urlStats.run();
        // exists?
        assertNotNull(urlStats.inputs);

        // is not empty?
        assert(0 < urlStats.inputs.size());

        int i = 0;
        for(String s : urlStats.inputs)
        {
            assertEquals (s, fakeEntries[i]);
            i++;
        }

    }

    @Test
    public void emptyLineBreaksInputStream()
    {
        String[] fakeEntries = {
                "sdf://google.com",
                "http://sdf.sdf.sdf.sdf",
                "http://google.com",
                "",
                "http://this.shouldnotbeontthe.list",
                "orthis",
        };

        insertAsInput(fakeEntries);
        urlStats.run();

        assertEquals(4, urlStats.inputs.size());
    }


    @Test
    public void triageOfGoodAndBadUrlsIsSetToIndividualFields()
    {
        String[] entries = {
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

        ArrayList<String> fakeEntries = new ArrayList<>();

        Collections.addAll(fakeEntries, entries);


        // set the inputs
        urlStats.sortEntries(fakeEntries);

        // these should be here
        assert (urlStats.invalidUrls.contains(fakeEntries.get(0)));
        assert (urlStats.invalidUrls.contains(fakeEntries.get(1)));
        assert (urlStats.invalidUrls.contains(fakeEntries.get(2)));
        // not here
        assertFalse (urlStats.validUrls.contains(fakeEntries.get(0)));
        assertFalse (urlStats.validUrls.contains(fakeEntries.get(1)));
        assertFalse (urlStats.validUrls.contains(fakeEntries.get(2)));

        // these are correct and should be here
        assert (urlStats.validUrls.contains(fakeEntries.get(3)));
        assert (urlStats.validUrls.contains(fakeEntries.get(4)));
        assert (urlStats.validUrls.contains(fakeEntries.get(5)));
        // not here
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(3)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(4)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(5)));

        // all values are captured to either one or the other lists
        for (String entry : fakeEntries)
        {
            boolean found = false;
            if (urlStats.validUrls.contains(entry) || urlStats.invalidUrls.contains(entry))
                found = true;

            assert (found);
        }
    }


}
