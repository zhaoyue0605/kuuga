package com.github.zhaoyue0605.rpc.transport.server;

import com.github.zhaoyue0605.rpc.dto.ServiceProperties;
import com.github.zhaoyue0605.rpc.transport.CommonProvider;

/**
 * 提供服务注册和获取服务接口
 *
 * @author Yue
 * @date 2020/9/12
 */
public interface ServiceProvider extends CommonProvider {

    /**
     * 注册服务端服务
     *
     * @param service
     * @param serviceClass
     * @return
     */
    <T> void registerService(T service, Class<T> serviceClass);

    /**
     * 启动Netty
     */
    void startServer();

}
