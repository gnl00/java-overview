package com.demo.sticky_split;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * StickyClient
 *
 * @author gnl
 * @since 2023/5/23
 */
public class StickyClient {
    private final String host;
    private final int port;

    public StickyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StickyClientHandler());
                        }
                    });

            ChannelFuture f = b.connect().sync();
            System.out.println("[client] connected to server " + f.channel().remoteAddress());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new StickyClient("localhost", 6666).start();
    }
}
