package net.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/25
 */
public class Server {
    private static Logger LOGGER = Logger.getLogger("Server");
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);
        LOGGER.info("Server Starting");
        // for (;;) {}
        while (!Thread.currentThread().isInterrupted() && !ss.isClosed()) { // 循环处理读写事件
            Socket socket = ss.accept(); // 当有新的客户端连接，返回一个 Socket 实例；如果没有客户端连接进来，accept() 方法会阻塞等待。
            LOGGER.info("[connected]: " + socket.getRemoteSocketAddress());
            Thread t = new Handler(socket); // 开启新线程处理新连接
            t.start();
        }
    }
}

class Handler extends Thread {
    private static final Logger LOGGER = Logger.getLogger("Handler");

    private final Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(
            InputStream is = this.socket.getInputStream();
            OutputStream os = this.socket.getOutputStream();
        ) {
            handle(is, os);
        } catch (IOException e) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                LOGGER.info("Socket closed");
                ex.printStackTrace();
            }
        }
    }

    private void handle(InputStream is, OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        response(writer,"hello\n");
        for(;;) {
            String s = reader.readLine();
            if(s != null && s.equals("bye")) {
                response(writer,"bye\n");
                break;
            }
            LOGGER.info("[received]: " + s);
            response(writer, ("ok==>" + s + "\n"));
        }
    }

    private void response(BufferedWriter writer, String data) throws IOException {
        // write 方法有个需要注意的点是：如果手动 flush 需要在末尾加上 \n 表示语句结束，否则一直等到写缓存满了才会 write
        writer.write(data);
        writer.flush(); // 不等缓存满，立即响应
    }
}
