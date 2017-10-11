import org.apache.http.HttpConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.HttpFetcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


import static org.junit.Assert.*;

public class TestHttpFetcher {

    final private boolean silent = true;
    private HttpFetcher httpFetcher;
    final private String eol = System.getProperty("line.separator");
    private String response;

    @Before
    public void setup()
    {
        // this will silence the output to Stdout
        if (this.silent)
            System.setOut(new PrintStream(new ByteArrayOutputStream()));

        this.httpFetcher = new HttpFetcher();

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
        this.httpFetcher = null;
    }


    @Test
    public void testWeGetSomethingBack()
    {
        LinkedHashMap<String, String> header = this.httpFetcher.getHttpHeaders(this.response);

        assertNotEquals (null, header);
    }

    //  Header fields are colon-separated name-value pairs in clear-text string format,
    // terminated by a carriage return (CR) and line feed (LF) character sequence.
    @Test
    public void testResponseMayBeHttpResponse()
    {
        LinkedHashMap<String, String> header = this.httpFetcher.getHttpHeaders(this.response);

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
        LinkedHashMap<String, String> header = this.httpFetcher.getHttpHeaders(this.response);


        String statusLine = header.get("status");

        assert (statusLine.matches("^HTTP.*?"));
        assert (statusLine.matches(".*?[\\d]{3}.*?"));
    }

    // The response message consists of the following:
    // Status-line, headers, an empty line, Optional HTTP message body data
    @Test
    public void testResponseHeadersDoesNotIncludeBody()
    {
        LinkedHashMap<String, String> header = this.httpFetcher.getHttpHeaders(this.response);

        assertFalse (header.containsValue("Hello world!"));
    }

    @Test
    public void testGetBodyDoesNotIncludeHeaders()
    {

        String body = this.httpFetcher.getBody(this.response);

        assertEquals("Hello world!", body);
    }

    @Test
    public void testIfAnyBlankLinesGetIntoHeader()
    {
        String response = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "\n" +
                "This is an html message";

        LinkedHashMap<String, String> headers = this.httpFetcher.getHttpHeaders(this.response);

        assertFalse (headers.containsValue("\\n"));
        assertFalse (headers.containsValue("This is an html message"));

    }

    @Test
    public void ifNoBodyInResponseThenShouldReturnEmptyString()
    {
        String response = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "\n";

        String body = this.httpFetcher.getBody(response);

        assert (body.length() < 1);

    }

    // A request-line begins with a method token, followed by a single space
    // (SP), the request-target, another single space (SP), the protocol
    // version, and ends with CRLF.
    // https://datatracker.ietf.org/doc/rfc7230/?include_text=1
    @Test
    public void malformedUrlReturnsNull()
    {
        String url = "http://fakeUrl.com";
        assertNull(httpFetcher.fetch(url));
    }



    @Test
    public void fakeUrlConnection() {
        final String url = "127.0.0.1:8080";
        try{
            ServerSocket server = new ServerSocket(8080);
            Map<String, List<String>> headers = httpFetcher.fetch("http://" + url);

            assertNotNull(headers);
            assertNotNull(headers.get("Content-Length"));

            server.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }




}
