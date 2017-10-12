import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.rodderscode.bbc.HttpFetcher;

import java.net.*;
import java.util.*;


import static org.junit.Assert.*;

public class TestHttpFetcher {

    final private static String HOST = "127.0.0.1";
    final private static int PORT = 9784;

    final private String eol = System.getProperty("line.separator");

    private HttpFetcher httpFetcher;
    private HashMap<String, List<String>> response;

    @Before
    public void setup()
    {
        // this will silence the output to Stdout
//        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        this.httpFetcher = new HttpFetcher();
    }


    @After
    public void tearDown()
    {
        this.httpFetcher = null;
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
        final String url = HOST + ":" + PORT;
        HttpServer server = null;
        try{
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new ServerHandler());
            server.start();
            Map<String, List<String>> headers = httpFetcher.fetch("http://" + url);

            assertNotNull(headers);

            server.stop(0);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (null != server)
                    server.stop(0);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    @Test
    public void getStatusLineFromHeaders() {
        final String url = HOST + ":" + PORT;
        HttpServer  server = null;
        try{
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new ServerHandler());
            server.start();
            Map<String, List<String>> headers = httpFetcher.fetch("http://" + url);

            assertNotNull(headers);

            String statusLine = httpFetcher.getHeadersLine(headers, HttpFetcher.STATUSLINE);

            assert (statusLine.matches("^HTTP.*?"));
            assert (statusLine.matches(".*?[\\d]{3}.*?"));

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (null != server)
                    server.stop(0);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    
    @Test
    public void getOtherLinesFromHeader()
    {
        final String url = HOST + ":" + PORT;
        HttpServer  server = null;
        try{
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new ServerHandler());
            server.start();
            Map<String, List<String>> headers = httpFetcher.fetch("http://" + url);

            String length = httpFetcher.getHeadersLine(headers, "Content-length");
            assertNotNull(length);

            String date = httpFetcher.getHeadersLine(headers, "Date");
            assertNotNull(date);


        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (null != server)
                    server.stop(0);
            }catch (Exception e){
                e.printStackTrace();
            }

        }   
    }

}
