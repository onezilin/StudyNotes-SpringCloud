package com.atguigu.springcloud.blockhandler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

/**
 * Description: 自定义限流异常处理类，代替默认的 SentinelGatewayBlockExceptionHandler
 */
public class JsonSentinelGatewayBlockExceptionHandler implements WebExceptionHandler {

    private List<ViewResolver> viewResolvers;
    private List<HttpMessageWriter<?>> messageWriters;

    private final Supplier<ServerResponse.Context> contextSupplier = () -> {
        return new ServerResponse.Context() {
            public List<HttpMessageWriter<?>> messageWriters() {
                return JsonSentinelGatewayBlockExceptionHandler.this.messageWriters;
            }

            public List<ViewResolver> viewResolvers() {
                return JsonSentinelGatewayBlockExceptionHandler.this.viewResolvers;
            }
        };
    };

    public JsonSentinelGatewayBlockExceptionHandler(List<ViewResolver> viewResolvers,
                                                    ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolvers;
        this.messageWriters = serverCodecConfigurer.getWriters();
    }

    /**
     * Description: writeResponse() 方法用于将自定义的响应数据写入到响应中
     */
    private Mono<Void> writeResponse(ServerResponse response, ServerWebExchange exchange) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        JSONObject responseObj = new JSONObject();
        responseObj.put("code", 403);
        responseObj.put("msg", "JsonSentinelGatewayBlockExceptionHandler：请求过于频繁，请稍后重试");
        byte[] datas = responseObj.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(datas);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        } else {
            return !BlockException.isBlockException(ex) ? Mono.error(ex) :
                    this.handleBlockedRequest(exchange, ex).flatMap((response) -> {
                        return this.writeResponse(response, exchange);
                    });
        }
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable throwable) {
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }
}
