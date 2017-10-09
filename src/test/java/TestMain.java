import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestMain {

    private Fetcher fetcher;

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
                "http://sdf.sdf.sdf.sdf"
        };

        String eol = System.getProperty("line.separator");

        String entries = String.join(eol, invalidEntries);

        InputStream inputStream = new ByteArrayInputStream(entries.getBytes());

        System.setIn(inputStream);

        Fetcher fetcher = new Fetcher();
        fetcher.processEntries();


        assert (fetcher.invalidUrls.contains(invalidEntries[0]));
        assert (fetcher.invalidUrls.contains(invalidEntries[1]));

    }
}
