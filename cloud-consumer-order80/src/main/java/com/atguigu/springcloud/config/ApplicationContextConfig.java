package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Description:
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    // 赋予 RestTemplate 负载均衡能力，否则无法获取 Eureka Server 中应用别名的真正地址
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
