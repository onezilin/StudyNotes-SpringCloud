package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * Description: 自定义负载均衡算法接口
 */
public interface LoadBalancer {

    /**
     * Description: 从所有的服务实例中选择一个服务实例
     *
     * @param serviceInstances 所有的服务实例
     * @return org.springframework.cloud.client.ServiceInstance
     */
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
