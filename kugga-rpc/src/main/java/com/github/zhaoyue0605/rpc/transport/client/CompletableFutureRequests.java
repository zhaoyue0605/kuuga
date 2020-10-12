package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.dto.KuggaResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放CompletableFuture对象，用于处理请求返回值
 *
 * @author Yue
 * @date 2020/9/17
 */
public class CompletableFutureRequests {

    private static final Map<String, CompletableFuture<KuggaResponse<Object>>> RESPONSE_FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<KuggaResponse<Object>> future) {
        RESPONSE_FUTURES.put(requestId, future);
    }

    public void complete(KuggaResponse<Object> response) {
        CompletableFuture<KuggaResponse<Object>> future = RESPONSE_FUTURES.remove(response.getRequestId());
        if (null != future) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }

}
