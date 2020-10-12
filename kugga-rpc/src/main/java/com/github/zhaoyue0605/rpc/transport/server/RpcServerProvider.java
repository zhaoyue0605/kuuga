package com.github.zhaoyue0605.rpc.transport.server;

import com.github.zhaoyue0605.rpc.exception.KuggaException;
import com.github.zhaoyue0605.rpc.factory.SingletonFactory;
import com.github.zhaoyue0605.rpc.registry.RegistryService;
import com.github.zhaoyue0605.rpc.serialize.Serializer;
import com.github.zhaoyue0605.rpc.spi.ExtensionLoader;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Netty 服务端服务提供者
 *
 * @author Yue
 * @date 2020/9/12
 */
public class RpcServerProvider implements ServiceProvider {

    private final RequestHandler requestHandler = SingletonFactory.getInstance(RequestHandler.class);

    private Serializer serializer;

    private RegistryService registryService;

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

    }

    @Override
    public <T> void registerService(T service, Class<T> serviceClass) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            requestHandler.addServiceProvider(serviceClass.getCanonicalName(), service);
            registryService.addService(serviceClass.getCanonicalName(), new InetSocketAddress(host, NettyServer.PORT));
        } catch (Exception e) {
            throw new KuggaException("获取IP时发生异常");
        }
    }

    @Override
    public void startServer() {
        if (null == serializer) {
            throw new KuggaException("未选择序列化协议");
        }
        if (null == registryService) {
            throw new KuggaException("未选择注册中心");
        }
        new NettyServer(serializer).start(requestHandler);
    }

}
