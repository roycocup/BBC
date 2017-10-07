import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

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


}
