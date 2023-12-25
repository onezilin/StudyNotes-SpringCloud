package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * Description: 账户表(TAccount)表服务接口
 */
@FeignClient(value = "seata-account-service", path = "/tAccount")
public interface TAccountService {

    @PostMapping("/decreaseMoney")
    CommonResult decreaseMoney(@RequestParam("id") Long id, @RequestParam("money") BigDecimal money);
}
