package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.transport.CommonProvider;

/**
 * 提供客户端获取注册中心及获取服务地址的接口
 *
 * @author Yue
 * @date 2020/9/16
 */
public interface ClientProvider extends CommonProvider {

    <T> T getRemoteService(Class<T> serviceClass);

}
