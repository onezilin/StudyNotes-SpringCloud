package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.MyLB;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * Description:
 */
@RestController
@RequestMapping("/consumer")
public class OrderController {

    // private static final String PAYMENT_URL = "http://localhost:8001";
    // Eureka Server 注册中心中对应注册服务应用别名
    private static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @Resource(name = "restTemplate2")
    private RestTemplate restTemplate2;

    @Resource
    private MyLB myLB;

    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> get(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    /**
     * Description: getForEntity 包含请求头、响应体（也就是 getForObject 的返回值）、响应状态码等信息
     *
     * @param id id
     * @return com.atguigu.springcloud.entities.CommonResult<com.atguigu.springcloud.entities.Payment>
     */
    @GetMapping("/payment/getForEntity/{id}")
    public CommonResult<Payment> get2(@PathVariable("id") Long id) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id,
                CommonResult.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new CommonResult<>(444, "操作失败");
        }
    }

    @GetMapping("/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (serviceInstances == null || serviceInstances.size() <= 0) {
            return null;
        }
        ServiceInstance serviceInstance = myLB.instances(serviceInstances);
        URI uri = serviceInstance.getUri();
        // 使用未加 @LoadBalanced 注解的 RestTemplate
        return restTemplate2.getForObject(uri + "/payment/lb", String.class);
    }
}
