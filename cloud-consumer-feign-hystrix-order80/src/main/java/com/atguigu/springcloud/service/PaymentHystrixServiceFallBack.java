package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * Description: 服务降级处理类
 */
@Component
public class PaymentHystrixServiceFallBack implements PaymentHystrixService {

    @Override
    public String queryById(Long id) {
        return "服务降级返回,---PaymentHystrixService";
    }
}
