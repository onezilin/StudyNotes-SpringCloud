package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Description:
 */
@Slf4j
@RestController
public class CircleBreakerController {

    @Resource
    private RestTemplate restTemplate;

    public static final String SERVICE_URL = "http://nacos-payment-provider";

    /**
     * Description: 没有 fallback 服务降级方法时，会抛出异常，导致客户端也会抛出异常
     */
    @GetMapping("/consumer/noFallback/{id}")
    @SentinelResource(value = "noFallback")
    public CommonResult<Payment> noFallback(@PathVariable Long id) {
        return handleRequest(id);
    }

    /**
     * Description: 有 fallback 服务降级方法时，会调用 fallback 方法，处理业务代码本身抛出的异常
     */
    @GetMapping("/consumer/fallback/{id}")
    @SentinelResource(value = "fallback", fallback = "handleFallback")
    public CommonResult<Payment> fallback(@PathVariable Long id) {
        return handleRequest(id);
    }

    /**
     * Description: 有 blockHandler 服务降级方法时，会调用 blockHandler 方法，处理抛出的 BlockHandler 异常
     */
    @GetMapping("/consumer/blockHandler/{id}")
    @SentinelResource(value = "blockHandler", blockHandler = "handleBlockHandler")
    public CommonResult<Payment> blockHandler(@PathVariable Long id) {
        return handleRequest(id);
    }

    private CommonResult<Payment> handleRequest(Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id,
                CommonResult.class, id);

        if (id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException, 非法参数异常....");
        } else if (result.getData() == null) {
            throw new NullPointerException("NullPointerException, 该ID没有对应记录, 空指针异常");
        }

        return result;
    }

    public CommonResult<Payment> handleFallback(@PathVariable Long id, Throwable e) {
        Payment payment = new Payment(id, null);
        return new CommonResult<>(444, "兜底异常 handleFallback, exception 内容:  " + e.getMessage(), payment);
    }

    public CommonResult<Payment> handleBlockHandler(@PathVariable Long id, Throwable e) {
        Payment payment = new Payment(id, null);
        return new CommonResult<>(445, "兜底异常 handleBlockHandler, exception 内容:  " + e.getMessage(), payment);
    }
}
