package com.atguigu.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 */
@Service
@DefaultProperties(defaultFallback = "paymentGlobalFallbackMethod")
public class PaymentService {

    @Value("${server.port}")
    private String serverPort;

    public String paymentInfoOK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + " paymentInfoOK，id：" + id + " O(∩_∩)O哈哈~";
    }

    /**
     * Description: id 为 0 时抛异常；不为 0  时，执行时间耗时 3000 ms
     */
    public String paymentInfo(Integer id) {
        int age = 10 / id;

        long start = System.currentTimeMillis();
        try {
            TimeUnit.SECONDS.sleep(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return "线程池：" + Thread.currentThread().getName() + " paymentInfoOK，id：" + id + " O(∩_∩)O哈哈~" + " 耗时(秒): " + id;
    }

    @HystrixCommand(fallbackMethod = "paymentInfoTimeOutHandler", commandProperties = {
            // 设置超时时间为 2000 ms
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    public String paymentInfoAnnotation(Integer id) {
        return paymentInfo(id);
    }

    @HystrixCommand
    public String paymentInfoAnnotationDefault(Integer id) {
        return paymentInfo(id);
    }

    public String paymentGlobalFallbackMethod() {
        return "Global 异常处理信息，请稍后再试，o(╥﹏╥)o";
    }

    /**
     * Description: 服务降级的容错方法
     */
    public String paymentInfoTimeOutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + " paymentInfoTimeOutHandler" + serverPort +
                " 系统繁忙或者运行报错，请稍后再试，id：" + id + "o(╥﹏╥)o";
    }
}
