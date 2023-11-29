package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Description:
 */
@Slf4j
@RestController
public class CircleBreakerController {

    @Resource
    private RestTemplate restTemplate;


}
