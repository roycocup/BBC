import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.Fetcher;

import java.util.LinkedHashMap;


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
        LinkedHashMap<String, String> header = this.fetcher.getHttpHeaders(this.response);

        assertNotEquals (null, header);
    }

    //  Header fields are colon-separated name-value pairs in clear-text string format,
    // terminated by a carriage return (CR) and line feed (LF) character sequence.
    @Test
    public void testResponseMayBeHttpResponse()
    {
        LinkedHashMap<String, String> header = this.fetcher.getHttpHeaders(this.response);

        // a well formed response needs to have more than one field
        assert (header.size() > 1);
        
    }

    // The first line of a response message is the status-line, consisting
    // of the protocol version, a space (SP), the status code, another
    // space, a possibly empty textual phrase describing the status code,
    // and ending with CRLF.
    // e,g status-line = HTTP-version SP status-code SP reason-phrase CRLF
    @Test
    public void testResponseHasStatusLine()
    {
        LinkedHashMap<String, String> header = this.fetcher.getHttpHeaders(this.response);


        String statusLine = header.get("status");

        assert (statusLine.matches("^HTTP.*?"));
        assert (statusLine.matches(".*?[\\d]{3}.*?"));
    }

    // The response message consists of the following:
    // Status-line, headers, an empty line, Optional HTTP message body data
    @Test
    public void testResponseHeadersDoesNotIncludeBody()
    {
        LinkedHashMap<String, String> header = this.fetcher.getHttpHeaders(this.response);

        assertFalse (header.containsValue("Hello world!"));
    }

    @Test
    public void testGetBodyDoesNotIncludeHeaders()
    {

        String body = this.fetcher.getBody(this.response);

        assertEquals("Hello world!", body);
    }

    @Test
    public void testIfAnyBlankLinesGetIntoHeader()
    {
        String response = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "\n" +
                "This is an html message";

        LinkedHashMap<String, String> headers = this.fetcher.getHttpHeaders(this.response);

        assertFalse (headers.containsValue("\\n"));
        assertFalse (headers.containsValue("This is an html message"));

    }

    @Test
    public void ifNoBodyInResponseThenShouldReturnEmptyString()
    {
        String response = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "\n";

        String body = this.fetcher.getBody(response);

        assert (body.length() < 1);

    }

}
