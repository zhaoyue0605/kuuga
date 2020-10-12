package com.github.zhaoyue0605.rpc.dto;

import lombok.Data;

/**
 * Rpc框架请求对象
 *
 * @author Yue
 * @date 2020/9/10
 */
@Data
public class KuggaRequest {

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] paramTypes;

    public ServiceProperties getServiceProperties() {
        return new ServiceProperties(this.interfaceName);
    }

}
