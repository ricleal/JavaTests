package eu.ill.tests;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

/**
 * Hello world!
 *
 */
public class App 
{
	static final int PORT = 8000;
	static final String CONTEXT = "/test";

    public static void main(String[] args) throws Exception {
    	System.out.println("Http Server starting...");
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(CONTEXT, new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Http Server started!");
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
           
           System.out.println("MyHandler : Just got a request!");
            // add the required response header for a PDF file
      		Headers h = t.getResponseHeaders();
      		h.add("Content-Type", "application/json");

            String response = "{\"code\" : \"200\", \"text\" : \"This is the response\"}";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
