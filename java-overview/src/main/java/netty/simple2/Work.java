package netty.simple2;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Work 线程，用来处理连接和事件
 *
 * @author gnl
 * @since 2023/4/4
 */
public class Work implements Runnable {

    private Thread thread;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector = Selector.open();

    private SelectionKey selectionKey;

    public Work(ServerSocketChannel serverSocketChannel) throws IOException {
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
        while (true) { // 新线程阻塞
            try {
                System.out.println("[work] blocking");
                selector.select();
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
                        socketChannel.write(ByteBuffer.wrap("server response for the first connection".getBytes()));
                        System.out.println("[notification] client connected");
                    }

                    // 可读事件
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int len = channel.read(buffer);
                        if (len == -1) {
                            channel.close();
                            System.out.println("[notification] client disconnected");
                            break;
                        }
                        byte[] bytes = new byte[len];
                        buffer.flip();
                        buffer.get(bytes);
                        System.out.println("[notification] client disconnected");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
