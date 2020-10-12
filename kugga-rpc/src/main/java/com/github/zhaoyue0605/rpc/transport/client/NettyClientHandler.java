package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.dto.KuggaResponse;
import com.github.zhaoyue0605.rpc.factory.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty Client请求处理器
 *
 * @author Yue
 * @date 2020/9/14
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler {

    private final ChannelProvider channelProvider;

    public NettyClientHandler() {
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) {
        log.info("客户端接收到响应: [{}]", o);
        if (o instanceof KuggaResponse) {
            KuggaResponse<Object> rpcResponse = (KuggaResponse<Object>) o;

        }
    }

}
