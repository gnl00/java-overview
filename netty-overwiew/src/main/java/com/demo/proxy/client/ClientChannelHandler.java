package com.demo.proxy.client;

import com.demo.proxy.serialize.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Frame) {
            System.out.println("[client] received Frame");
        }
        if (msg instanceof String) {
            System.out.println("[client] received String");

            System.out.println(msg);
            ctx.writeAndFlush("hello");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
