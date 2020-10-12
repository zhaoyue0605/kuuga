package com.github.zhaoyue0605.rpc.spi;

import java.lang.annotation.*;

/**
 * 自定义SPI注解，为了使用dubbo实现的SPI机制
 *
 * @author Yue
 * @date 2020/9/10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {
}
