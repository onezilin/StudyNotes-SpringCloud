package com.atguigu.springcloud.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.atguigu.springcloud.blockhandler.JsonSentinelGatewayBlockExceptionHandler;
import com.atguigu.springcloud.blockhandler.OpenBlockRequestHandler;
import com.atguigu.springcloud.filter.MyPathKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
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

    /**
     * Description: Description: 自定义异常响应类，代替默认的 DefaultBlockRequestHandler，
     * <p>
     * 由于 JsonSentinelGatewayBlockExceptionHandler 中重写了 writeResponse() 方法，会覆盖 OpenBlockRequestHandler 的响应信息，
     * 因此测试 OpenBlockRequestHandler 时，需要将上面的注入方法注释掉
     */
    @PostConstruct
    public void doInit() {
        GatewayCallbackManager.setBlockHandler(new OpenBlockRequestHandler());
    }
}
