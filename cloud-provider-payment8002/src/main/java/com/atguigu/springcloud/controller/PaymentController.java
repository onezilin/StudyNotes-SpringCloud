package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Payment)表控制层
 *
 * @author onezilin
 * @since 2023-06-04 19:20:45
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {
    /**
     * 服务对象
     */
    @Resource
    private PaymentService paymentService;

    @Resource
    private DiscoveryClient discoryClient;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 分页查询
     *
     * @param payment     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Payment>> queryByPage(Payment payment, PageRequest pageRequest) {
        return ResponseEntity.ok(this.paymentService.queryByPage(payment, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public CommonResult<Payment> queryById(@PathVariable("id") Long id) {
        return new CommonResult<>(200, "查询成功，serverPort：" + serverPort, this.paymentService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param payment 实体
     * @return 新增结果
     */
    @PostMapping("/create")
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        return ResponseEntity.ok(this.paymentService.insert(payment));
    }

    /**
     * 编辑数据
     *
     * @param payment 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Payment> edit(Payment payment) {
        return ResponseEntity.ok(this.paymentService.update(payment));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.paymentService.deleteById(id));
    }

    @GetMapping("/discovery")
    public DiscoveryClient discovery() {
        // 获取注册的微服务名称，也就是 spring.application.name
        List<String> serviceNameList = discoryClient.getServices();

        serviceNameList.forEach(serviceName -> {
            // 获取微服务名称具体的主机名称、ip、端口等信息
            List<ServiceInstance> serviceInstancesList = discoryClient.getInstances(serviceName);
            serviceInstancesList.forEach(serviceInstance -> {
                log.info("服务名：{}，主机名：{}，端口：{}，URL地址：{}", serviceInstance.getServiceId(), serviceInstance.getHost(),
                        serviceInstance.getPort(), serviceInstance.getUri());
            });
        });
        return discoryClient;
    }

    @GetMapping("/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping("/timeout")
    public String timeout() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return serverPort;
    }

    @GetMapping("/fallback")
    public String fallback() {
        return "服务降级测试OK";
    }

    /**
     * Description: 测试 Zipkin 链路监控
     */
    @GetMapping("/zipkin")
    public String paymentZipkin() {
        return "hi,i`am paymentzipkin server fall back.welcome to atguigu.hahahahahhahahah";
    }
}

