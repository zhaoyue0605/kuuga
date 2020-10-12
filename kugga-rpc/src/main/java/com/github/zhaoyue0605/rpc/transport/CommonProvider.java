package com.github.zhaoyue0605.rpc.transport;

/**
 * 服务端和客户端公用接口
 *
 * @author Yue
 * @date 2020/9/17
 */
public interface CommonProvider {

    /**
     * 选择注册中心
     *
     * @param registryService
     */
    void registerRegistryService(String registryService);

    /**
     * 选择序列化协议
     *
     * @param protocol
     */
    void registerSerializationProtocol(String protocol);

    /**
     * 选择负载均衡策略
     *
     * @param loadBalance
     */
    void registerLoadBalance(String loadBalance);

}
