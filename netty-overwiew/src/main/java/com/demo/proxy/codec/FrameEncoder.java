package com.demo.proxy.codec;

import com.demo.proxy.serialize.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class FrameEncoder extends MessageToByteEncoder<Frame> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Frame f, ByteBuf out) throws Exception {
        out.writeInt(f.getLen());
        out.writeInt(f.getCode());
        out.writeBytes(f.getBody());
    }
}
