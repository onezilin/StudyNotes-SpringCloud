package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.context.ContextUtil;
import com.atguigu.springcloud.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @GetMapping("/defineResource")
    public String defineResource() {
        return resourceService.defineResource();
    }

    @GetMapping("/defineResourceByAnnotation")
    public String defineResourceByAnnotation() {
        return resourceService.defineResourceByAnnotation();
    }
}
