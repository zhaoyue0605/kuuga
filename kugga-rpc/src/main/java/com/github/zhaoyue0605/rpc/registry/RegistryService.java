package com.github.zhaoyue0605.rpc.registry;

import com.github.zhaoyue0605.rpc.loadbalance.LoadBalance;
import com.github.zhaoyue0605.rpc.spi.SPI;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 注册中心接口
 *
 * @author Yue
 * @date 2020/9/12
 */
@SPI
public interface RegistryService {

    /**
     * 向注册中心注册服务
     *
     * @param serviceName
     * @param inetSocketAddress
     * @throws IOException
     */
    void addService(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 查询服务注册地址
     *
     * @param loadBalance
     * @param serviceName
     * @return
     */
    InetSocketAddress searchService(LoadBalance loadBalance, String serviceName);

}
