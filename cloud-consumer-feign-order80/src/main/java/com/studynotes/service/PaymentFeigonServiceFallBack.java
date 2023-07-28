package com.studynotes.service;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.stereotype.Component;

/**
 * Description: 服务降级处理类
 */
@Component
public class PaymentFeigonServiceFallBack implements PaymentFeignService {

    @Override
    public CommonResult<Payment> queryById(Long id) {
        return new CommonResult<>(444, "服务降级返回,---PaymentFeigonService", new Payment(id, "errorSerial"));
    }

    @Override
    public CommonResult<Payment> create(Payment payment) {
        return new CommonResult<>(444, "服务降级返回,---PaymentFeigonService", new Payment(payment.getId(), "errorSerial"));
    }
}
