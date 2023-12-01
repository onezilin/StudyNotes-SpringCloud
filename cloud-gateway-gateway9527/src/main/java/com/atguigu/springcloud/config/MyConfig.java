package com.atguigu.springcloud.config;

import com.atguigu.springcloud.blockhandler.JsonSentinelGatewayBlockExceptionHandler;
import com.atguigu.springcloud.filter.MyPathKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * Description: 自定义配置类
 */
@Configuration
public class MyConfig {

    /**
     * Description: 将自定义的 KeyResolver 注入 Spring 容器
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return new MyPathKeyResolver();
    }

    /**
     * Description: 自定义限流异常处理类，代替默认的 SentinelGatewayBlockExceptionHandler
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public JsonSentinelGatewayBlockExceptionHandler jsonSentinelGatewayBlockExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        return new JsonSentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }
}
