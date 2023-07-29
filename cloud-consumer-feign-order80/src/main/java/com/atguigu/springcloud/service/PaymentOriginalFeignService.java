package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import feign.Param;
import feign.RequestLine;

/**
 * Description: 使用原生的 Feign 进行服务调用
 */
public interface PaymentOriginalFeignService {

    @RequestLine("GET /payment/get/{id}")
    CommonResult<Payment> queryById(@Param("id") Long id);
}
