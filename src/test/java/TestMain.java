import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import uk.co.rodderscode.bbc.HttpFetcher;
import uk.co.rodderscode.bbc.UrlStats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class TestMain {

    private UrlStats urlStats;
    private String eol = System.getProperty("line.separator");
    private PrintStream originalOut;
    private ByteArrayOutputStream collectedOut;
    private HttpFetcher mockFetcher;

    @Before
    public void setup(){
        // this will silence the output to Stdout
        originalOut = System.out;
        collectedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(collectedOut));

        //main object being tested
        mockFetcher = mock(HttpFetcher.class);
        urlStats = new UrlStats(mockFetcher);
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
                "https://www.pets4homes.co.uk/images/articles/1646/large/kitten-emergencies-signs-to-look-out-for-537479947ec1c.jpg",
                "http://site.mockito.org/",
                "http://127.0.0.1:9000",

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
        assert (urlStats.validUrls.contains(fakeEntries.get(6)));
        assert (urlStats.validUrls.contains(fakeEntries.get(7)));
        assert (urlStats.validUrls.contains(fakeEntries.get(8)));
        assert (urlStats.validUrls.contains(fakeEntries.get(9)));
        assert (urlStats.validUrls.contains(fakeEntries.get(10)));
        assert (urlStats.validUrls.contains(fakeEntries.get(11)));
        // not here
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(3)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(4)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(5)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(6)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(7)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(8)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(9)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(10)));
        assertFalse (urlStats.invalidUrls.contains(fakeEntries.get(11)));

        // all values are captured to either one or the other lists
        for (String entry : fakeEntries)
        {
            boolean found = false;
            if (urlStats.validUrls.contains(entry) || urlStats.invalidUrls.contains(entry))
                found = true;

            assert (found);
        }
    }
    
    @Test
    public void testFinaStatsAreNotNull()
    {
        try{
            Field field = urlStats.getClass().getDeclaredField("finalStats");
            assertNotNull (field);
            assertNotNull(urlStats.finalStats);
        } catch (NoSuchFieldException e) {
            assert (false);
        }

    }

    @Test
    public void collectInfoForEachValidSite() throws Exception
    {
        ArrayList<HashMap<String, String>> info;

        String[] entries = {
                "http://google.com",
                "http://www.bbc.co.uk/iplayer",
                "https://google.com",
                "http://www.bbc.co.uk/missing/thing",
                "http://not.exists.bbc.co.uk/",
                "http://www.oracle.com/technetwork/java/javase/downloads/index.html",
                "https://www.pets4homes.co.uk/images/articles/1646/large/kitten-emergencies-signs-to-look-out-for-537479947ec1c.jpg",
                "http://site.mockito.org/",
                "http://127.0.0.1:9000",
        };
        ArrayList<String> validUrls = new ArrayList<>(Arrays.asList(entries));


        Map<String, List<String>> headers = new HashMap<>();
        when(mockFetcher.fetch(anyString()))
                .thenReturn(headers);


        // call
        info = urlStats.collectInfo(validUrls);

        // was it called as many as the entries?
        verify(mockFetcher, times(entries.length)).fetch(anyString());

        // does the info have a size?
        assert (info.size() > 0);
    }

}
