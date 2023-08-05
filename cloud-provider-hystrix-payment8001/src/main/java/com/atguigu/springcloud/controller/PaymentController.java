package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentOrignalObservableService;
import com.atguigu.springcloud.service.PaymentOrignalService;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @GetMapping("/hystrix/ok/{id}")
    public String paymentInfoOK(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfoOK(id);
        log.info("result: " + result);
        return result;
    }

    /**
     * Description: 不使用服务降级
     */
    @GetMapping("/hystrix/timeout/{id}")
    public String paymentInfoTimeOut(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo(id);
        log.info("result: " + result);
        return result;
    }

    /**
     * Description: 继承 HystrixCommand 的方式实现服务降级
     */
    @GetMapping("/hystrix/timeoutOrignal/{id}")
    public String paymentInfoTimeOutOrignal(@PathVariable("id") Integer id) {
        PaymentOrignalService service = new PaymentOrignalService(paymentService, id);
        String result = "";

        // 调用 get() 同步阻塞的方式执行 run()
        result = service.execute();

        // 调用 queue() 异步的方式执行 run()
        // Future<String> resultFuture = service.queue();
        // try {
        //     result = resultFuture.get();
        // } catch (InterruptedException | ExecutionException e) {
        //     throw new RuntimeException(e);
        // }

        log.info("result: " + result);
        return result;
    }

    /**
     * Description: 继承 HystrixObservableCommand 的方式实现服务降级
     */
    @GetMapping("/hystrix/timeoutOrignal2/{id}")
    public String paymentInfoTimeOutOrignalBatch(@PathVariable("id") Integer id) {
        PaymentOrignalObservableService service = new PaymentOrignalObservableService(paymentService, id);

        List<String> resultList = new ArrayList<>();
        Action1<String> action = result -> {
            log.info("result: " + result);
            resultList.add(result);
        };

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                log.info("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("onError: " + throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                log.info("onNext: " + s);
            }
        };

        // 获取 Observable，并注册观察者
        service.observe().subscribe(action);
        service.observe().subscribe(observer);
        return CollectionUtils.isEmpty(resultList) ? "" : resultList.get(0);
    }

    /**
     * Description: 使用 @HystrixCommand 注解实现服务降级
     */
    @GetMapping("/hystrix/timeoutAnnotation/{id}")
    public String paymentInfoTimeOutAnnotation(@PathVariable("id") Integer id) {
        return paymentService.paymentInfoAnnotation(id);
    }

    /**
     * Description: 使用 @DefaultProperties 全局默认配置注解实现服务降级
     */
    @GetMapping("/hystrix/timeoutAnnotationDefault/{id}")
    public String paymentInfoTimeOutAnnotationDefault(@PathVariable("id") Integer id) {
        return paymentService.paymentInfoAnnotationDefault(id);
    }

    /**
     * Description: 使用 @HystrixCommand 注解实现服务降级，使用 @HystrixCommand 的 observableExecutionMode 返回 Observable
     */
    @GetMapping("/hystrix/timeoutAnnotationObservable/{id}")
    public String paymentInfoTimeOutAnnotationObservable(@PathVariable("id") Integer id) {
        Observable<String> observable = paymentService.paymentInfoAnnotationObservable(id);
        List<String> resultList = new ArrayList<>();
        Action1<String> action = result -> {
            log.info("result: " + result);
            resultList.add(result);
        };

        observable.subscribe(action);
        return CollectionUtils.isEmpty(resultList) ? "" : resultList.get(0);
    }
}
