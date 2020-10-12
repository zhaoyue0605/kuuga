package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.dto.KuggaRequest;
import com.github.zhaoyue0605.rpc.dto.KuggaResponse;
import com.github.zhaoyue0605.rpc.exception.KuggaException;
import com.github.zhaoyue0605.rpc.serialize.Serializer;
import com.github.zhaoyue0605.rpc.transport.transcoding.RequestDecoder;
import com.github.zhaoyue0605.rpc.transport.transcoding.RequestEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Netty 客户端
 *
 * @author Yue
 * @date 2020/9/14
 */
@Slf4j
public class NettyClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    private Serializer serializer;

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public NettyClient(InetSocketAddress inetSocketAddress) throws InterruptedException {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new RequestDecoder(serializer, KuggaResponse.class));
                        ch.pipeline().addLast(new RequestEncoder(serializer, KuggaRequest.class));
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(inetSocketAddress);
        if (!channelFuture.await(30, TimeUnit.SECONDS)) {
            throw new KuggaException("连接超时");
        }
        channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
    }

    public Channel connect(InetSocketAddress inetSocketAddress) {
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
        try {
            if (!channelFuture.await(30, TimeUnit.SECONDS)) {
                throw new KuggaException("请求超时！");
            }
        } catch (InterruptedException e) {
            throw new KuggaException("中断异常！");
        }
        Channel channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        return channel;
    }

}
