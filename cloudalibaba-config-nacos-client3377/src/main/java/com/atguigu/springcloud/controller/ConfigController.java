package com.atguigu.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 */
@RestController
@RefreshScope // 支持 Config、Nacos 的动态刷新功能
public class ConfigController {

    @Value("${config.info}")
    private String configInfo;

    @Value("${config.run}")
    private String configRun;

    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo + " " + configRun;
    }
}
