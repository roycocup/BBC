package uk.co.rodderscode.bbc;



public class Fetcher {

    public String getHttp(String url) {
        return  "HTTP/1.1 200 OK\n" +
                "Date: Tue, 10 Oct 2017 19:33:46 GMT\n" +
                "Server: Apache\n" +
                "X-Powered-By: PHP/7.1.1\n" +
                "Keep-Alive: timeout=5, max=100\n" +
                "Connection: Keep-Alive\n" +
                "Transfer-Encoding: chunked\n" +
                "Content-Type: text/html; charset=UTF-8";

    }
}
