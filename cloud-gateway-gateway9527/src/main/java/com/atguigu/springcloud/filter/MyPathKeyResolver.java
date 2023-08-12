package com.atguigu.springcloud.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: KeyResolver，以请求参数 myName 的值作为限流的 key，如果没有 myName 参数，则过滤掉此请求
 * 
 */
public class MyPathKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        String value = exchange.getRequest().getQueryParams().getFirst("myName");
        System.out.println(value);
        return Mono.just(value);
    }
}
