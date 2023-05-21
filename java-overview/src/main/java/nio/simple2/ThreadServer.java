package nio.simple2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 改进：可以使用线程池来管理
 */
public class ThreadServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        Worker worker = new Worker(socketChannel);

        Selector selector = Selector.open();
        // 将 Channel 注册到 Selector
        SelectionKey selectionKey = socketChannel.register(selector, 0, socketChannel);
        // 设置感兴趣的事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        // 服务器绑定端口
        socketChannel.bind(new InetSocketAddress(8888));
        worker.start();
    }
}
