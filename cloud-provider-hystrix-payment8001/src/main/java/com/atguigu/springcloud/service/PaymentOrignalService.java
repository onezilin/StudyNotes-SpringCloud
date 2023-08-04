package com.atguigu.springcloud.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

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
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutEnabled(true)
                                // 使用线程隔离时，会为每个命令分配一个线程，容易造成资源浪费
                                // 使用信号量隔离时，会为每个命令分配一个信号量，使用当前线程执行
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE) // 信号量隔离
                                .withExecutionIsolationSemaphoreMaxConcurrentRequests(10) // 信号量最大并发数
                                .withExecutionTimeoutInMilliseconds(2000)
                                .withCircuitBreakerEnabled(true) // 开启断路器
                                .withCircuitBreakerRequestVolumeThreshold(10) // 请求次数
                                .withCircuitBreakerSleepWindowInMilliseconds(10000) // 时间窗口期
                                .withCircuitBreakerErrorThresholdPercentage(50) // 失败率达到多少后跳闸
                ));

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
