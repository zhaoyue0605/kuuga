package com.github.zhaoyue0605.rpc.transport.transcoding;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import com.github.zhaoyue0605.rpc.serialize.Serializer;

import java.util.List;

/**
 * 解码器
 *
 * @author Yue
 * @date 2020/9/12
 */
@Slf4j
public class RequestDecoder extends ByteToMessageDecoder {

    /**
     * Netty传输的消息长度也就是对象序列化后对应的字节数组的大小，存储在 ByteBuf 头部
     */
    private static final int BODY_LENGTH = 4;

    private Serializer serializer;
    private Class<?> encodeClass;

    public RequestDecoder(Serializer serializer, Class<?> encodeClass) {
        this.serializer = serializer;
        this.encodeClass = encodeClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0 || byteBuf.readableBytes() < 0) {
            log.error("非法消息长度，请求无效");
            return;
        }
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        Object obj = serializer.parse(body, encodeClass);
        list.add(obj);
    }

}
