package io.github.escapehonbab.netty.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;

import java.io.*;

public class ObjectSerializer {
    @Getter
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ByteBuf writeAsByteBuf(Object object) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);

        byte[] bs = bos.toByteArray();

        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(bs);

        return byteBuf;
    }

    public static ByteBuf writeJsonAsByteBuf(Object object) throws Exception {
        byte[] bs = mapper.writeValueAsBytes(object);
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(bs);
        return byteBuf;
    }

    public static Object readJsonByteBufAsObject(ByteBuf byteBuf, Class<?> classType) throws IOException {
        int len = byteBuf.readableBytes();
        byte[] read = new byte[len];
        for (int i = 0; i < len; i++) {
            read[i] = byteBuf.getByte(i);
        }
        return mapper.readValue(read, classType);
    }

    public static Object readAsObject(ByteBuf byteBuf) throws Exception {
        int len = byteBuf.readableBytes();
        byte[] read = new byte[len];
        for (int i = 0; i < len; i++) {
            read[i] = byteBuf.getByte(i);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(read);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }
}
