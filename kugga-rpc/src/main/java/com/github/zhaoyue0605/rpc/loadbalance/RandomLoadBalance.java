package com.github.zhaoyue0605.rpc.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * 随机访问策略
 *
 * @author Yue
 * @date 2020/9/19
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String select(List<String> addresses) {
        Random random = new Random();
        return addresses.get(random.nextInt(addresses.size()));
    }

}
