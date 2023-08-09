package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 除了配置文件的配置方式，也可以通过编码方式配置 route
 */
@Configuration
public class MyRoutes {

    /**
     * Description: 注入 RouteLocator 实现类
     */
    @Bean
    public RouteLocator customRouteLocator1(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route_atguigu1", r -> r.path("/guonei").uri("http://news.baidu.com/guonei"))
                .build();
    }

    @Bean
    public RouteLocator customRouteLocator2(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route_atguigu2", r -> r.path("/guoji").uri("http://news.baidu.com/guoji"))
                .build();
    }
}
