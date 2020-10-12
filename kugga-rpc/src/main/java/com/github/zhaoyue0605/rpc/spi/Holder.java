package com.github.zhaoyue0605.rpc.spi;

/**
 * SPI机制的加载器，改造版，摘自dubbo的实现方式，此类是存放实例化类的
 *
 * @author Yue
 * @date 2020/9/10
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
