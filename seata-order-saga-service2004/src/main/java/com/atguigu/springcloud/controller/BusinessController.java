package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.TOrder;
import com.atguigu.springcloud.service.BusinessService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    /**
     * Description: 下单接口，通过 Saga 模式实现分布式事务
     *
     * @param order 订单
     * @return com.atguigu.springcloud.entities.CommonResult<com.atguigu.springcloud.entities.TOrder>
     */
    @PostMapping("/purchaseBySaga")
    public CommonResult<TOrder> purchaseBySaga(@RequestBody TOrder order) {
        businessService.purchaseBySaga(order);
        return new CommonResult<>(200, "下单成功");
    }
}
