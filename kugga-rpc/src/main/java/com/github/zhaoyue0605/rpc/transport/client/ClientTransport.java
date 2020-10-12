package com.github.zhaoyue0605.rpc.transport.client;

import com.github.zhaoyue0605.rpc.dto.KuggaRequest;

/**
 * Demo interface
 *
 * @author Yue
 * @date 2020/9/17
 */
public interface ClientTransport {

    /**
     * 发送客户端请求
     *
     * @param rpcRequest
     * @return
     */
    Object sendRpcRequest(KuggaRequest rpcRequest);

}
