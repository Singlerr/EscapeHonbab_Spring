package io.github.escapehonbab.netty;

import io.github.escapehonbab.netty.utils.ObjectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] data = ObjectSerializer.getMapper().writeValueAsBytes(o);
        byteBuf.writeBytes(data);
    }
}
