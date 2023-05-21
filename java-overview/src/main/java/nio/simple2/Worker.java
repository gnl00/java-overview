package nio.simple2;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Work 线程，用来处理连接和事件
 *
 * @author gnl
 * @since 2023/4/4
 */
public class Worker implements Runnable {

    private final Thread thread;

    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector = Selector.open();

    private final SelectionKey selectionKey;

    public Worker(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        serverSocketChannel.configureBlocking(false);
        this.selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.thread = new Thread(this);
    }

    public void start() {
        this.thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("[worker] blocking");
                if (selector.select() == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    // 连接事件
                    if (key.isAcceptable()) {
                        // Returns the channel for which SelectionKey was created
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        // Accepts a connection made to this channel's socket
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        // 注册到 selector
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("[acceptable] client connected");

                        socketChannel.write(ByteBuffer.wrap("server response for the first connection".getBytes()));
                    }

                    // 可读事件
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int len = channel.read(buffer);
                        if (len == -1) {
                            channel.close();
                            System.out.println("[readable] client disconnected");
                            break;
                        }
                        byte[] bytes = new byte[len];
                        buffer.flip();
                        buffer.get(bytes);
                        System.out.println("[readable] client disconnected");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
