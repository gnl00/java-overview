package com.demo.proxy.proxy;

import com.demo.proxy.serialize.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("welcome");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Frame) {
            System.out.println("[proxy-server]  received Frame");
            if (((Frame) msg).getCode() == 0) {
                initForwardServer();
            }
        }
        if (msg instanceof String) {
            System.out.println("[proxy-server] received String");
            System.out.println(msg);
        }
    }

    private void initForwardServer() {
        ForwardConfig config = new ForwardConfig("127.0.0.1", 3306, 3307);
    }
}
