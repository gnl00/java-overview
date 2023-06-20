package com.demo.proxy.codec;

import com.demo.proxy.serialize.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class FrameDecoder extends ReplayingDecoder<Frame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readInt();
        byte[] bytes = new byte[len];
        int opCode = in.readInt();
        in.readBytes(bytes);

        Frame frame = new Frame(len, opCode, bytes);
        out.add(frame);
    }
}
