package com.atguigu.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Description:
 */
@RestController
@RequestMapping("/consumer")
public class OrderController {

    private static final String PAYMENT_URL = "http://nacos-payment-provider";

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @GetMapping("/payment/nacos/{id}")
    public String get(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/nacos/" + id, String.class);
    }
}
