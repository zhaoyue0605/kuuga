package com.github.zhaoyue0605.rpc.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例工厂
 *
 * @author Yue
 * @date 2020/9/13
 */
public final class SingletonFactory {

    private static final Map<String, Object> SINGLETON_MAP = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance = SINGLETON_MAP.get(key);
        synchronized (c) {
            if (instance == null) {
                try {
                    instance = c.getDeclaredConstructor().newInstance();
                    SINGLETON_MAP.put(key, instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return c.cast(instance);
    }

}
