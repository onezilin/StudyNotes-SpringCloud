package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: 库存表(TStorage)表服务接口
 */
@FeignClient(value = "seata-storage-service", path = "/tStorage")
public interface TStorageService {

    @PostMapping("/decreaseStorage")
    CommonResult decreaseStorage(@RequestParam("id") Long id, @RequestParam("count") Integer count);
}
