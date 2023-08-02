package com.atguigu.springcloud.service;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Description: 继承 HystrixObservableCommand 的方式，实现服务降级
 * HystrixCommand 返回一个单一的结果，而 HystrixObservableCommand 返回一个 Observable 类型的结果
 */
public class PaymentOrignalObservableService extends HystrixObservableCommand<String> {

    private final PaymentService paymentService;

    private Integer id;

    public PaymentOrignalObservableService(PaymentService paymentService, Integer id) {
        super(HystrixObservableCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("orderService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("queryByOrderId"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutEnabled(true)));

        this.paymentService = paymentService;
        this.id = id;
    }

    /**
     * Description: 返回一个被观察者 Observable 对象
     */
    @Override
    public Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {

                if (!subscriber.isUnsubscribed()) {
                    String result = paymentService.paymentInfo(id);
                    subscriber.onNext(result);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.immediate());
    }

    @Override
    public Observable<String> resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    String result = paymentService.paymentInfoTimeOutHandler(id);
                    subscriber.onNext(result);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.immediate());
    }
}
