package com.atguigu.springcloud.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Description: 自定义请求拦截器
 */
public class InterceptorTwo implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        System.out.println("InterceptorTwo");
    }
}
