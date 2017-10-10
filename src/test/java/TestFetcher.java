import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;
import static org.junit.Assert.*;

public class TestFetcher {

    private Fetcher fetcher;
    final private String eol = System.getProperty("line.separator");
    private String response;

    @Before
    public void setup()
    {
        // this will silence the output to Stdout
//        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        this.fetcher = new Fetcher();

        this.response = "HTTP/1.1 200 OK\n" +
                "Date: Sun, 10 Oct 2010 23:26:07 GMT\n" +
                "Server: Apache/2.2.8 (Ubuntu) mod_ssl/2.2.8 OpenSSL/0.9.8g\n" +
                "Last-Modified: Sun, 26 Sep 2010 22:04:35 GMT\n" +
                "ETag: \"45b6-834-49130cc1182c0\"\n" +
                "Accept-Ranges: bytes\n" +
                "Content-Length: 13\n" +
                "Connection: close\n" +
                "Content-Type: text/html\n" +
                "\n" +
                "Hello world!";
    }


    @After
    public void tearDown()
    {
        this.fetcher = null;
    }


    @Test
    public void testWeGetSomethingBack()
    {
        String header = this.fetcher.getHttpHeaders(this.response);

        assertNotEquals (null, header);
    }

    //  Header fields are colon-separated name-value pairs in clear-text string format,
    // terminated by a carriage return (CR) and line feed (LF) character sequence.
    @Test
    public void testResponseMayBeHttpResponse()
    {
        String header = this.fetcher.getHttpHeaders(this.response);

        // a well formed response needs to have more than one field
        assert (header.split(eol).length > 1);
        
    }

    // The first line of a response message is the status-line, consisting
    // of the protocol version, a space (SP), the status code, another
    // space, a possibly empty textual phrase describing the status code,
    // and ending with CRLF.
    // e,g status-line = HTTP-version SP status-code SP reason-phrase CRLF
    @Test
    public void testResponseHasStatusLine()
    {
        String header = this.fetcher.getHttpHeaders(this.response);
        String[] fields = header.split(eol);

        String[] statusLine = fields[0].split(" ");

        assert (statusLine[0].matches("^HTTP.*?"));
        assert (statusLine[1].matches("[\\d]{3}"));
    }

    // The response message consists of the following:
    // Status-line, headers, an empty line, Optional HTTP message body data
    @Test
    public void testResponseHeadersDoesNotIncludeBody()
    {

        String header = this.fetcher.getHttpHeaders(this.response);
        String[] fields = header.split(eol);

        // lets check the last line is not preceeded by an empty line

        // last line has SOMETHING
        assertFalse (fields[fields.length - 1].length() < 10);
        assert (fields[fields.length - 1].length() > 0);

        // this line must have some length or the headers contains the message
        assert (fields[fields.length - 2].length() > 0);

    }

    @Test
    public void testGetBodyDoesNotIncludeHeaders()
    {

        String body = this.fetcher.getBody(this.response);

        assertEquals("Hello world!", body);
    }

}
