package netty.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/4
 */
public class SimpleClient {
    public static void main(String[] args) throws IOException {
        SocketChannel clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        // get selector
        try (Selector selector = Selector.open()) {
            // 客户端连接服务器
            clientChannel.connect(new InetSocketAddress(8888));
            // 将客服端注册到 selector 上
            SelectionKey selectionKey = clientChannel.register(selector, 0);
            // 设置事件
            selectionKey.interestOps(SelectionKey.OP_CONNECT);

            // 开始轮询事件
            while (true) {
                // 阻塞，直到有事件
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // remove after get
                    iterator.remove();

                    // 如果是连接成功事件
                    if (key.isConnectable()) {
                        if (clientChannel.finishConnect()) {
                            // 注册读事件
                            clientChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("[event] read event register success");
                            clientChannel.write(ByteBuffer.wrap("This is message from client".getBytes()));
                        }
                    }

                    // 如果是可读事件
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // allocate buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int len = channel.read(buffer);
                        System.out.println("[client receive byte length] " + len);

                        byte[] readByte = new byte[len];
                        buffer.flip();
                        buffer.get(readByte);
                        System.out.println("[client received] " + new String(readByte));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
