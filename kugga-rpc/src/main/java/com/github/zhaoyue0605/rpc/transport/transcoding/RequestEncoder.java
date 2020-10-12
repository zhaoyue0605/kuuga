package com.github.zhaoyue0605.rpc.transport.transcoding;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.github.zhaoyue0605.rpc.serialize.Serializer;

/**
 * 编码器
 *
 * @author Yue
 * @date 2020/9/12
 */
public class RequestEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    private Class<?> encodeClass;

    public RequestEncoder(Serializer serializer, Class<?> encodeClass) {
        this.serializer = serializer;
        this.encodeClass = encodeClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {
        if (encodeClass.isInstance(o)) {
            byte[] body = serializer.serialize(o);
            int dataLength = body.length;
            byteBuf.writeInt(dataLength);
            byteBuf.writeBytes(body);
        }
    }

}
