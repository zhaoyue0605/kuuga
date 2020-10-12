package com.github.zhaoyue0605.rpc.loadbalance;

import java.util.List;

/**
 * 负载均衡抽象类
 *
 * @author Yue
 * @date 2020/9/19
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectAddress(List<String> addresses) {
        if (addresses == null || addresses.size() == 0) {
            return null;
        }
        if (addresses.size() == 1) {
            return addresses.get(0);
        }
        return select(addresses);
    }

    /**
     * 具体的实现策略
     *
     * @param addresses
     * @return
     */
    protected abstract String select(List<String> addresses);

}
