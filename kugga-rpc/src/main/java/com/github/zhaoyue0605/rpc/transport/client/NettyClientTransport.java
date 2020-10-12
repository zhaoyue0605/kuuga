package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.dto.KuggaRequest;
import com.github.zhaoyue0605.rpc.dto.KuggaResponse;
import com.github.zhaoyue0605.rpc.factory.SingletonFactory;
import com.github.zhaoyue0605.rpc.registry.RegistryService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * 被调度发送客户端请求
 *
 * @author Yue
 * @date 2020/9/17
 */
@Slf4j
public class NettyClientTransport implements ClientTransport {

    private ChannelProvider channelProvider;
    private RegistryService registryService;
    private CompletableFutureRequests completableFutureRequests;

    public NettyClientTransport(RegistryService registryService) {
        this.registryService = registryService;
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.completableFutureRequests = SingletonFactory.getInstance(CompletableFutureRequests.class);
    }

    @Override
    public Object sendRpcRequest(KuggaRequest rpcRequest) {
        CompletableFuture<KuggaResponse<Object>> resultFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.getServiceProperties().getRpcServiceName();
        InetSocketAddress inetSocketAddress = registryService.searchService(rpcServiceName);
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            completableFutureRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("客户端发送请求: [{}]", rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("客户端请求发送失败:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

}
