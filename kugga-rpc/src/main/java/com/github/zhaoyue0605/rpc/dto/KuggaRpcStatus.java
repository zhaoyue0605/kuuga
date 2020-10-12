package com.github.zhaoyue0605.rpc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码
 *
 * @author Yue
 * @date 2020/9/12
 */
@AllArgsConstructor
@Getter
public enum KuggaRpcStatus {

    /**
     * 调用成功
     */
    SUCCESS(200, "request success"),

    /**
     * 调用失败
     */
    FAIL(500, "request fail");

    private int code;

    private String message;

}
