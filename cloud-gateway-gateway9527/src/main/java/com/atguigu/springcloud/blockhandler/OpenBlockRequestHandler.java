package com.atguigu.springcloud.blockhandler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.atguigu.springcloud.entities.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Description: 自定义异常响应类，代替默认的 DefaultBlockRequestHandler
 */
public class OpenBlockRequestHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
        // JSON result by default.
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromObject(buildErrorResult(ex)));
    }

    private CommonResult<String> buildErrorResult(Throwable ex) {
        return new CommonResult<>(444, "OpenBlockRequestHandler：请求过于频繁，请稍后重试", null);
    }
}
