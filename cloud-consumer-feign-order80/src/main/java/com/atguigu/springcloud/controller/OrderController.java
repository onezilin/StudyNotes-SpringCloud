package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentFeignService;
import com.atguigu.springcloud.service.PaymentFeignServiceConfigurationTest;
import com.atguigu.springcloud.service.PaymentOriginalFeignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    /**
     * Description: 使用原生的 Feign 进行服务调用
     *
     * @param id id
     * @return com.atguigu.springcloud.entities.CommonResult<com.atguigu.springcloud.entities.Payment>
     */
    @GetMapping("/payment/originalFeign/get/{id}")
    public CommonResult<Payment> originalFeignGet(@PathVariable("id") Long id) {
        String url = "http://localhost:8001";
        PaymentOriginalFeignService service = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(PaymentOriginalFeignService.class, url);

        return service.queryById(id);
    }
}
