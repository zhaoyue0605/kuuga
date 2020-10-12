package com.github.zhaoyue0605.rpc.loadbalance;

import com.github.zhaoyue0605.rpc.spi.SPI;

import java.util.List;

/**
 * 负载均衡策略
 *
 * @author Yue
 * @date 2020/9/19
 */
@SPI
public interface LoadBalance {

    String selectAddress(List<String> addresses);

}
