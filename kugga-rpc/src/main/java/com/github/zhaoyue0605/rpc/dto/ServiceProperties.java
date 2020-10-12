package com.github.zhaoyue0605.rpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 定义了一类放服务名，未来可增加版本控制
 *
 * @author Yue
 * @date 2020/9/12
 */
@Data
@AllArgsConstructor
public class ServiceProperties {

    private String serviceName;

    public String getRpcServiceName() {
        return this.getServiceName();
    }

}
