package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.factory.SingletonFactory;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端channel缓存
 *
 * @author Yue
 * @date 2020/9/14
 */
public class ChannelProvider {

    private final NettyClient nettyClient;

    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
        nettyClient = SingletonFactory.getInstance(NettyClient.class);
    }

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        Channel channel = nettyClient.connect(inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }

}
