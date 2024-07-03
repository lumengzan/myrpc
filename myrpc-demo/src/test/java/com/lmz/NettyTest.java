package com.lmz;

import com.lmz.netty.AppClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author 老疯狗
 * @Title NettyTest
 * @date 2024/7/2
 */
public class NettyTest {

    @Test
    public void testCompositeBuffer() {
        ByteBuf head = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        byteBuf.addComponents(head, body);
    }

    @Test
    public void testWrapper() {
        byte[] buf=new byte[1024];
        byte[] buf1=new byte[1024];
        ByteBuf byteBuf = Unpooled.wrappedBuffer(buf,buf1);

    }

    public void testMessage() throws IOException {
        ByteBuf message = Unpooled.buffer();
        message.writeBytes("lmz".getBytes(StandardCharsets.UTF_8));
        message.writeByte(1);
        message.writeShort(125);
        message.writeInt(256);
        message.writeByte(1);
        message.writeLong(123124124L);
    }
}
