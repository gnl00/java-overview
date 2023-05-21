package net.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/26
 */
public class HTTPTest {
// Java 11
//    static HttpClient httpClient = HttpClient.newHttpClient();
//
//    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
//
//        String url = "https://www.baidu.com";
//        HttpRequest request = HttpRequest.newBuilder(new URI(url))
//                .header("Accept", "*/*")
//                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
//                .timeout(Duration.ofSeconds(5))
//                .version(HttpClient.Version.HTTP_2)
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        Map<String, List<String>> map = response.headers().map();
//        map.forEach((key, value) -> {
//            System.out.println(key + ": " + value.get(0));
//        });
//
//        System.out.println(response.body().substring(0, 1024) + "...");
//    }

// Java 8
//    public static void main(String[] args) throws IOException {
//        URL url = new URL("http://www.baidu.com");
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setUseCaches(false);
//        conn.setConnectTimeout(3000); // connect timeout
//
//        conn.setRequestProperty("Accept", "*/*");
//        conn.setRequestProperty("User-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
//
//        conn.connect();
//        if (conn.getResponseCode() != 200) {
//            System.out.println("bad response code");
//        }
//
//        Map<String, List<String>> fields = conn.getHeaderFields();
//        fields.forEach((key, value) -> {
//            System.out.println(key + ": " + value);
//        });
//
//        InputStream is = conn.getInputStream();
//    }
}
