package com.atguigu.myFeign;

import com.atguigu.springcloud.interceptors.InterceptorOne;
import com.atguigu.springcloud.interceptors.InterceptorTwo;
import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 自定义 Feign 的配置类
 * 注意：为某个 @FeignClient 定义的 Feign 的配置类不能放在主启动类所在的包以及子包下
 */
@Configuration
public class MySelfFeign {

    /**
     * Description: 将契约改为 feign 的默认契约，这样只能使用 feign 自带的注解了
     * 也就是不使用 Spring MVC 的注解，例如：@RequestMapping，
     * 而是使用 Feign 的 @RequestLine、@Param、@QueryMap、@HeaderMap
     */
    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    /**
     * Description: 配置 Feign 的日志级别
     */
    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    @Bean
    public InterceptorTwo interceptorTwo() {
        return new InterceptorTwo();
    }

    @Bean
    public InterceptorOne interceptorOne() {
        return new InterceptorOne();
    }
}
