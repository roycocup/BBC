import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import static org.junit.Assert.*;

public class TestFetcher {

    private Fetcher fetcher;

    @Before
    public void setup()
    {
        Fetcher fetcher = new Fetcher();
    }


    @After
    public void tearDown()
    {
        fetcher = null;
    }

}
