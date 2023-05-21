package net.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/25
 */
public class Client {
    private static Logger LOGGER = Logger.getLogger("Client");

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        try(InputStream is = socket.getInputStream()) {
            try(OutputStream os = socket.getOutputStream()) {
                handle(is, os);;
            }
        }
        socket.close();
        LOGGER.info("Client disconnected");
    }

    private static void handle(InputStream is, OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter writer = new BufferedWriter(osw);

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);

        Scanner sc = new Scanner(System.in);

        LOGGER.info("[server] " + reader.readLine()); // readLine() 只有读到 \n 才会返回

        for (;;) {
            System.out.println(">>>");
            String s = sc.nextLine();

            request(writer, s);

            String response = reader.readLine();
            System.out.println("<<<\n" + response);

            if (response.equals("bye")) {
                break;
            }
        }
    }

    private static void request(BufferedWriter writer, String data) throws IOException {
        writer.write(data);
        writer.newLine();
        writer.flush();
    }
}
