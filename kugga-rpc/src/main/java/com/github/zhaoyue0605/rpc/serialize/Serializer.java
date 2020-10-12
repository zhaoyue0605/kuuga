package com.github.zhaoyue0605.rpc.serialize;

import com.github.zhaoyue0605.rpc.spi.SPI;

/**
 * 序列化接口
 *
 * @author Yue
 * @date 2020/9/10
 */
@SPI
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T parse(byte[] bytes, Class<T> clazz);

}
