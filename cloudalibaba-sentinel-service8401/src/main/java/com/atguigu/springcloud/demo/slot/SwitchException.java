package com.atguigu.springcloud.demo.slot;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * Description: 当 Resource 匹配规则抛出后的异常类，用于实现流量控制
 */
public class SwitchException extends BlockException {

    public SwitchException(String ruleLimitApp, String switchKey) {
        super(ruleLimitApp, switchKey);
    }
}
