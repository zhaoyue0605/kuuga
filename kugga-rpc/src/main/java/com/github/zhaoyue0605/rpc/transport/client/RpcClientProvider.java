package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.loadbalance.LoadBalance;
import com.github.zhaoyue0605.rpc.registry.RegistryService;
import com.github.zhaoyue0605.rpc.serialize.Serializer;
import com.github.zhaoyue0605.rpc.spi.ExtensionLoader;
import com.github.zhaoyue0605.rpc.transport.client.proxy.RpcClientProxy;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Netty 客户端服务提供者
 *
 * @author Yue
 * @date 2020/9/17
 */
public class RpcClientProvider implements ClientProvider {

    private Serializer serializer;

    private RegistryService registryService;

    private LoadBalance loadBalance;

    private final Map<InetSocketAddress, NettyClient> clientMap = new ConcurrentHashMap<>();

    @Override
    public void registerRegistryService(String registryService) {
        this.registryService = ExtensionLoader.getExtensionLoader(RegistryService.class).getExtension(registryService);
    }

    @Override
    public void registerSerializationProtocol(String protocol) {
        this.serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(protocol);
    }

    @Override
    public void registerLoadBalance(String loadBalance) {
        this.serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(loadBalance);
    }

    @Override
    public <T> T getRemoteService(Class<T> serviceClass) {
        InetSocketAddress inetSocketAddress = registryService.searchService(loadBalance, serviceClass.getCanonicalName());
        NettyClient transport = clientMap.computeIfAbsent(inetSocketAddress, this::createTransport);
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, new RpcClientProxy());
    }

    private Transport createTransport(InetSocketAddress inetSocketAddress) {
        try {
            return client.createTransport(inetSocketAddress);
        } catch (InterruptedException) {
            throw new RuntimeException(e);
        }
    }

}
