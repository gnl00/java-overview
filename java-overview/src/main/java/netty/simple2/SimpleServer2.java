package netty.simple2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO
 */
public class SimpleServer2 {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        Work work = new Work(socketChannel);

        Selector selector = Selector.open();
        // 将 Channel 注册到 Selector
        SelectionKey selectionKey = socketChannel.register(selector, 0, socketChannel);
        // 设置感兴趣的事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        // 服务器绑定端口
        socketChannel.bind(new InetSocketAddress(8888));
        work.start();

        // selector 开始轮询
        while (true) {
            // select 方法会阻塞，直到有事件发生
            System.out.println("[server] blocking");
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // remove after get a key
                iterator.remove();

                // 如果轮询到事件，注册到 selector 中
                if (key.isAcceptable()) {
                    // get server channel
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

                    // get client channel
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);

                    // register client channel
                    SelectionKey clientSelectionKey = clientChannel.register(selector, 0, clientChannel);
                    clientSelectionKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("[notification] client connected");
                    // 客户端连接成功
                    clientChannel.write(ByteBuffer.wrap("connect success".getBytes()));
                    System.out.println("[notification] server responded");
                }

                // 如果轮询到可读事件
                if (key.isReadable()) {
                    // get client channel
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    // set client data to buffer
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // write into buffer
                    int len = clientChannel.read(buffer);
                    System.out.println("[server receive byte length] " + len);
                    if (len > 0) {
                        buffer.flip();
                        System.out.println("[server received] " + Charset.defaultCharset().decode(buffer).toString());
                    } else {
                        clientChannel.close();
                        break;
                    }
                }
            }
        }
    }
}
