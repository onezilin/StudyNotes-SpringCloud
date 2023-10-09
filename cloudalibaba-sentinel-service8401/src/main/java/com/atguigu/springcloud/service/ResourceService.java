package com.atguigu.springcloud.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

/**
 * Description: 资源是 Sentinel 的关键概念。
 * 它可以是 Java 应用程序中的任何内容，例如，由应用程序提供的服务，或由应用程序调用的其它应用提供的服务，RPC接口方法，甚至可以是一段代码。
 * <p>
 * 详见：<a href="https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html">资源与规则</a>
 */
@Service
public class ResourceService {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ///1、设置资源名
            try (Entry entry = SphU.entry("defineResource")) {
                // 2、被保护的业务逻辑
                System.out.println(entry);
                System.out.println("defineResource");
            } catch (BlockException e) {
                // 3、如果被保护的资源被限流了，就会抛出 BlockException
                e.printStackTrace();
                System.out.println("defineResource BlockException");
            }
        }
    }

    /**
     * Description: SphU.entry() 方式用来定义资源，并且在 try-with-resources 语句中保护资源。
     *
     * @return
     */
    public String defineResource() {
        ///1、设置资源名
        try (Entry entry = SphU.entry("defineResource")) {
            // 2、被保护的业务逻辑
            return "defineResource";
        } catch (BlockException e) {
            // 3、如果被保护的资源被限流了，就会抛出 BlockException
            e.printStackTrace();
            return "defineResource BlockException";
        }
    }

    /**
     * Description: @SentinelResource 注解方式定义资源
     */
    @SentinelResource(value = "defineResourceByAnnotation", blockHandler = "myBlockHandler", fallback = "myFallback")
    public String defineResourceByAnnotation() {
        return "defineResourceByAnnotation";
    }

    /**
     * Description:  blockHandler 函数，原方法调用被限流的时候调用
     *
     * @param e BlockException 异常对象
     * @return java.lang.String
     */
    public String myBlockHandler(BlockException e) {
        e.printStackTrace();
        return "defineResourceByAnnotation BlockException";
    }

    /**
     * Description: fallback 函数，原方法调用被限流/降级/系统保护的时候调用
     *
     * @return java.lang.String
     */
    public String myFallback() {
        return "defineResourceByAnnotation Fallback";
    }
}
