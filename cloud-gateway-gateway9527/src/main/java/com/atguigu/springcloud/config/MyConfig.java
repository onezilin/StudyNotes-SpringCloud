package com.atguigu.springcloud.config;

import com.atguigu.springcloud.filter.MyPathKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
