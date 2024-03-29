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
     * Description: 下单接口，通过 AT 模式实现分布式事务
     *
     * @param order 订单
     * @return com.atguigu.springcloud.entities.CommonResult<com.atguigu.springcloud.entities.TOrder>
     */
    @PostMapping("/purchaseByAT")
    public CommonResult<TOrder> purchaseByAT(@RequestBody TOrder order) {
        businessService.purchaseByAT(order);
        return new CommonResult<>(200, "下单成功");
    }

    /**
     * Description: 下单接口，通过 TCC 模式实现分布式事务
     *
     * @param order 订单
     * @return com.atguigu.springcloud.entities.CommonResult<com.atguigu.springcloud.entities.TOrder>
     */
    @PostMapping("/purchaseByTCC")
    public CommonResult<TOrder> purchaseByTCC(@RequestBody TOrder order) {
        businessService.purchaseByTCC(order);
        return new CommonResult<>(200, "下单成功");
    }
}
