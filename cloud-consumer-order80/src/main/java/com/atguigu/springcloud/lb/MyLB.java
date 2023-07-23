package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 自定义负载均衡算法实现类，根据访问次数获取服务实例
 */
@Component
public class MyLB implements LoadBalancer {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * Description: 获取并增加访问次数
     *
     * @return int
     */
    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next)); // CAS
        System.out.println("*****第几次访问，次数next: " + next);
        return next;
    }

    /**
     * Description: 从所有的服务实例中选择一个服务实例
     *
     * @param serviceInstances 所有的服务实例
     * @return org.springframework.cloud.client.ServiceInstance
     */
    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        // 获取当前服务实例的下标
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
