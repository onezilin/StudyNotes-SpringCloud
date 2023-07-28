package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description: Feign 声明式接口，通过 Feign 调用服务提供者的接口
 */
@FeignClient(value = "CLOUD-PAYMENT-SERVICE", path = "/payment", fallback = PaymentFeigonServiceFallBack.class)
public interface PaymentFeignService {

    @GetMapping("/get/{id}")
    CommonResult<Payment> queryById(@PathVariable("id") Long id);

    @GetMapping("/create")
    CommonResult<Payment> create(@RequestBody Payment payment);
}
