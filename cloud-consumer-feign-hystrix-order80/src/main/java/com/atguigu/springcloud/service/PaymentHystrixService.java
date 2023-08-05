package com.atguigu.springcloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description: Feign 声明式接口，通过 Feign 调用服务提供者的接口
 */
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", path = "/payment", fallback =
        PaymentHystrixServiceFallBack.class)
public interface PaymentHystrixService {

    @GetMapping("/hystrix/timeout/{id}")
    String queryById(@PathVariable("id") Long id);
}
