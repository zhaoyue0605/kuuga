package com.github.zhaoyue0605.rpc.transport.server;

import com.github.zhaoyue0605.rpc.dto.KuggaRequest;
import com.github.zhaoyue0605.rpc.exception.KuggaException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理RPC处理
 *
 * @author Yue
 * @date 2020/9/13
 */
@Slf4j
public class RequestHandler {

    private Map<String, Object> serviceProviders = new HashMap<>();

    public void addServiceProvider(String serviceName, Object serviceProvider) {
        serviceProviders.put(serviceName, serviceProvider);
        log.info("增加一个服务: {}, 服务提供者: {}.", serviceName, serviceProvider.getClass().getCanonicalName());
    }

    public Object handle(KuggaRequest request) {
        Object result;
        try {
            Object service = serviceProviders.get(request.getServiceProperties().getRpcServiceName());
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            result = method.invoke(service, request.getParameters());
            log.info("服务:[{}] 成功处理方法:[{}]", request.getInterfaceName(), request.getMethodName());
        } catch (Exception e) {
            throw new KuggaException("请求处理失败");
        }
       return result;
    }

}
