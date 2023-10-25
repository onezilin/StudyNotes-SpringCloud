package com.atguigu.springcloud.demo.slot;

import lombok.Data;

import java.util.Set;

/**
 * Description: 自定义规则类
 * 1、如果不配置 resources，则开关作用于全部资源
 * 2、如果配置了include，则开关作用于 include 指定的所有资源
 * 3、如果配置了exclude，则开关作用于除 exclude 指定的所有资源
 */
@Data
public class SwitchRule {

    public static final String SWITCH_KEY_OPNE = "open";

    public static final String SWITCH_KEY_CLOSE = "close";

    // 开关状态
    private String status;

    // 开关控制的资源
    private Resources resources;

    @Data
    public static class Resources {

        // 包含
        private Set<String> include;

        // 排除
        private Set<String> exclude;
    }
}
