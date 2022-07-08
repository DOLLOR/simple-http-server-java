import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
---------
javac SimpleHttpServer.java&&java SimpleHttpServer
---------
```js
fetch('http://localhost:8723/path/to/file?a=1&b=2', {
  method: 'GET',
}).then(i => i.text())
  .then(i => console.log(i));

fetch('http://localhost:8723/path/to/file?a=1&b=2', {
  method: 'POST',
  body: JSON.stringify({
    aa: 11,
    bb: 'cc'
  }, undefined, '  '),
}).then(i => i.text())
  .then(i => console.log(i));

```
---------
 */

/**
 * SimpleHttpServer
 */
public class SimpleHttpServer {
    public static void main(String[] args) throws Exception {
        var server = HttpServer.create(new InetSocketAddress(8723), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("server start ok");
        System.out.println("http://localhost:8723/");
    }

    public static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange e) throws IOException {
            System.out.println(e.getRequestMethod());
            System.out.println(e.getRequestURI());
            var bodyString = getRequestBodyString(e);
            System.out.println(bodyString);

            // response
            var responseText = "http server ok";
            var responseHeaders = e.getResponseHeaders();
            responseHeaders.set("Access-Control-Allow-Origin", "*");
            responseHeaders.set("Access-Control-Allow-Headers", "*");
            responseHeaders.set("Cache-Control", "public, max-age=0");
            responseHeaders.set("Content-Type", "text/plain; charset=UTF-8");
            responseHeaders.set("My-Server", "java.version/".concat(System.getProperty("java.version")));

            e.sendResponseHeaders(200, responseText.length());
            var outputStream = e.getResponseBody();
            outputStream.write(responseText.getBytes());
            outputStream.close();
        }
    }

    public static String getRequestBodyString(HttpExchange e) {
        var bodyStream = e.getRequestBody();
        var inputStreamReader = new InputStreamReader(bodyStream);
        var bufferReader = new BufferedReader(inputStreamReader);
        var bodyString = bufferReader.lines().collect(Collectors.joining("\n"));
        return bodyString;
    }
}
