package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.TOrder;
import com.atguigu.springcloud.service.TOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单表(TOrder)表控制层
 *
 * @author onezilin
 * @since 2023-12-20 09:54:25
 */
@RestController
@RequestMapping("/tOrder")
public class TOrderController {
    /**
     * 服务对象
     */
    @Resource
    private TOrderService tOrderService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public CommonResult<TOrder> queryById(@PathVariable("id") Long id) {
        return new CommonResult<>(200, "查询成功", tOrderService.queryById(id));
    }
}

