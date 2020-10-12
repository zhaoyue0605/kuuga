package com.github.zhaoyue0605.rpc.dto;

import lombok.Data;

/**
 * Rpc框架返回对象
 *
 * @author Yue
 * @date 2020/9/10
 */
@Data
public class KuggaResponse<T> {

    private String requestId;

    private Integer code;

    private String message;

    private T data;

    public static <T> KuggaResponse<T> success(T data, String requestId) {
        KuggaResponse<T> response = new KuggaResponse<>();
        KuggaRpcStatus rpcStatus = KuggaRpcStatus.SUCCESS;
        response.setCode(rpcStatus.getCode());
        response.setMessage(rpcStatus.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> KuggaResponse<T> fail(String requestId) {
        KuggaResponse<T> response = new KuggaResponse<>();
        KuggaRpcStatus rpcStatus = KuggaRpcStatus.FAIL;
        response.setCode(rpcStatus.getCode());
        response.setMessage(rpcStatus.getMessage());
        response.setRequestId(requestId);
        return response;
    }

}
