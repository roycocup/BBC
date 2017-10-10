import org.apache.http.protocol.ResponseServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TestFetcher {

    private Fetcher fetcher;
    private String eol = System.getProperty("line.separator");

    @Before
    public void setup()
    {
        // this will silence the output to Stdout
//        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        this.fetcher = new Fetcher();
    }


    @After
    public void tearDown()
    {
        this.fetcher = null;
    }


    @Test
    public void testWeGetSomethingBack()
    {
        String url = "http://rodderscode.co.uk";
        String header = this.fetcher.getHttp(url);

        assertNotEquals (null, header);
    }

    //  Header fields are colon-separated name-value pairs in clear-text string format,
    // terminated by a carriage return (CR) and line feed (LF) character sequence.
    @Test
    public void testResponseMayBeHttpResponse()
    {
        String url = "http://rodderscode.co.uk";
        String header = this.fetcher.getHttp(url);

        String[] fields = header.split(eol);
        // a well formed response needs to have more than one field
        assert (fields.length > 1);
        
    }

    // The first line of a response message is the status-line, consisting
    // of the protocol version, a space (SP), the status code, another
    // space, a possibly empty textual phrase describing the status code,
    // and ending with CRLF.
    // e,g status-line = HTTP-version SP status-code SP reason-phrase CRLF
    @Test
    public void testResponseHasStatusLine()
    {
        String url = "http://rodderscode.co.uk";
        String header = this.fetcher.getHttp(url);
        String[] fields = header.split(eol);

        String[] statusLine = fields[0].split(" ");

        assert (statusLine[0].matches("^HTTP.*?"));
        assert (statusLine[1].matches("[\\d]{3}"));
    }

}
