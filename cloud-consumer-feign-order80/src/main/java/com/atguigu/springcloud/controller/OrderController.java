package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentFeignService;
import com.atguigu.springcloud.service.PaymentFeignServiceConfigurationTest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Description:
 */
@RestController
@RequestMapping("/consumer")
public class OrderController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @Resource
    private PaymentFeignServiceConfigurationTest paymentFeignServiceConfigurationTest;

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> get(@PathVariable("id") Long id) {
        return paymentFeignService.queryById(id);
    }

    @PostMapping("/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment) {
        return paymentFeignService.create(payment);
    }


    @GetMapping("/payment/configurationTest/get/{id}")
    public CommonResult<Payment> configurationTestGet(@PathVariable("id") Long id) {
        return paymentFeignServiceConfigurationTest.queryById(id);
    }
}
