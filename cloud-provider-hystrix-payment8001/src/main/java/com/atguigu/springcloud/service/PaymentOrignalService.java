package com.atguigu.springcloud.service;

import com.netflix.hystrix.*;

/**
 * Description: 继承 HystrixCommand 的方式，实现服务降级
 */
public class PaymentOrignalService extends HystrixCommand<String> {

    private final PaymentService paymentService;

    private Integer id;

    public PaymentOrignalService(PaymentService paymentService, Integer id) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("orderService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("queryByOrderId"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutEnabled(true))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10)));

        this.paymentService = paymentService;
        this.id = id;
    }

    @Override
    protected String run() {
        return paymentService.paymentInfo(id);
    }

    @Override
    protected String getFallback() {
        return paymentService.paymentInfoTimeOutHandler(id);
    }
}
