package com.github.zhaoyue0605.rpc.transport.server;

import com.github.zhaoyue0605.rpc.dto.KuggaRequest;
import com.github.zhaoyue0605.rpc.dto.KuggaResponse;
import com.github.zhaoyue0605.rpc.dto.KuggaRpcStatus;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty Server请求处理器
 *
 * @author Yue
 * @date 2020/9/12
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler {

    private RequestHandler requestHandler;

    public NettyServerHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("接收到客户端调用: [{}] ", o);
        KuggaRequest rpcRequest = (KuggaRequest) o;
        Object result = requestHandler.handle(rpcRequest);
        log.info(String.format("server get result: %s", result.toString()));
        if (channelHandlerContext.channel().isActive() && channelHandlerContext.channel().isWritable()) {
            KuggaResponse<Object> response = KuggaResponse.success(result, rpcRequest.getRequestId());
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            KuggaResponse<Object> response = KuggaResponse.fail(rpcRequest.getRequestId());
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            log.error("not writable now, message dropped");
        }
    }

}
