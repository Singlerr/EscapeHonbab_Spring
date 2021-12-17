package io.github.escapehonbab.netty;


import io.github.escapehonbab.netty.utils.ObjectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.Builder;
import org.json.JSONObject;

import java.util.List;

@Builder
public class MessageDecoder extends ByteToMessageDecoder {
    private List<Class<?>> registeredClasses;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        byte[] read = new byte[len];
        for (int i = 0; i < len; i++) {
            read[i] = byteBuf.getByte(i);
        }
        JSONObject obj = new JSONObject(new String(read));
        String clsName = obj.getString("@type");
        for (Class<?> cls : registeredClasses) {
            if (cls.getSimpleName().equalsIgnoreCase(clsName)) {
                Object o = ObjectSerializer.getMapper().readValue(read, cls);
                list.add(o);
            }
        }
        byteBuf.clear();
    }
}
