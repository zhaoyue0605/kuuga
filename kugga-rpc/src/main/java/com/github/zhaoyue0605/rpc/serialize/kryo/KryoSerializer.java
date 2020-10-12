package com.github.zhaoyue0605.rpc.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.zhaoyue0605.rpc.dto.KuggaRequest;
import com.github.zhaoyue0605.rpc.dto.KuggaResponse;
import com.github.zhaoyue0605.rpc.exception.KuggaException;
import com.github.zhaoyue0605.rpc.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Kryo 序列化实现类
 *
 * @author Yue
 * @date 2020/9/10
 */
public class KryoSerializer implements Serializer {

    /**
     * Kryo序列化框架不是线程安全的，多线程情况下需要用各自实例化的Kryo对象
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(KuggaRequest.class);
        kryo.register(KuggaResponse.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new KuggaException("对象序列化异常");
        }
    }

    @Override
    public <T> T parse(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new KuggaException("对象反序列化异常");
        }
    }

}
