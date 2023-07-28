package com.atguigu.springcloud.service;

import com.atguigu.myFeign.MySelfFeign;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Description: 测试自定义 Feign 的配置类
 * 注意：value 相同时，需要配置不同的 contextId
 */
@FeignClient(contextId = "configurationTest", value = "CLOUD-PAYMENT-SERVICE", path = "/payment", configuration =
        MySelfFeign.class)
public interface PaymentFeignServiceConfigurationTest {

    /**
     * Description: 配置了自定义的 Feign Contract，所以这里不能使用 Spring MVC 的注解了
     * 使用 Feign 的 @RequestLine、@Param、@QueryMap、@HeaderMap
     */
    // @GetMapping("/get/{id}")
    // CommonResult<Payment> queryById(@PathVariable("id") Long id);
    @RequestLine("GET /get/{id}")
    CommonResult<Payment> queryById(@Param("id") Long id);
}
