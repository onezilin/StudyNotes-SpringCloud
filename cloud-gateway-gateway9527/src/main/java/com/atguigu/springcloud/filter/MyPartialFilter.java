package com.atguigu.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: 自定义局部过滤器
 */
@Slf4j
public class MyPartialFilter implements GatewayFilter, Ordered {

    /**
     * Description: 只有当请求中携带了 user-id 头部信息时，才能访问
     *
     * @param exchange 封装了request 和 response 对象
     * @param chain 过滤器链
     * @return reactor.core.publisher.Mono<java.lang.Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().toString();

        log.info("局部过滤器：url：{}，method：{}", url, request.getMethodValue());
        //获取header
        String userId = exchange.getRequest().getHeaders().getFirst("user-id");
        log.info("userId：" + userId);

        if (StringUtils.isBlank(userId))
        {
            log.info("*****头部验证不通过，请在头部输入 user-id");
            //终止请求，直接回应
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    /**
     * Description: 过滤器优先级，值越小，优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
