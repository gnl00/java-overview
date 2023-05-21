package netty.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 相比于 bio，nio 中的线程可以使用 selector 来维护多个连接
 * 目前的 CPU 都是多核的，假设一个线程能维护的连接上限是 50，当达到上限的时候该线程的压力就会增大，此时可以考虑创建别的线程来分担
 *
 */
public class SimpleServer {

    public static void main(String[] args) throws IOException {
        // server channel
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        // set non-block
        socketChannel.configureBlocking(false);

        // open Selector
        Selector selector = Selector.open();

        // server channel register into selector
        // SelectionKey 是一个表示 SelectableChannel 与 Selector 注册的令牌
        SelectionKey selectionKey = socketChannel.register(selector, 0, socketChannel);
        // 设置感兴趣的事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        // socketChannel 绑定端口
        socketChannel.bind(new InetSocketAddress(8888));

        // selector 开始轮询
        while (true) {
            // select 方法会阻塞，直到有事件发生
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
                    System.out.println("[client connected]");
                    // 客户端连接成功
                    clientChannel.write(ByteBuffer.wrap("connect success".getBytes()));
                    System.out.println("[server responded]");
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
