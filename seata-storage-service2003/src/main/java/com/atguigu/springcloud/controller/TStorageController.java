package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.TStorage;
import com.atguigu.springcloud.service.TStorageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 库存(TStorage)表控制层
 *
 * @author onezilin
 * @since 2023-12-22 10:56:17
 */
@RestController
@RequestMapping("/tStorage")
public class TStorageController {
    /**
     * 服务对象
     */
    @Resource
    private TStorageService tStorageService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public CommonResult<TStorage> queryById(@PathVariable("id") Long id) {
        return new CommonResult<>(200, "查询成功", tStorageService.queryById(id));
    }

    /**
     * Description: 扣减库存
     *
     * @param id    库存 id
     * @param count 扣减数量
     */
    @PostMapping("/decreaseStorage")
    public CommonResult decreaseStorage(@RequestParam("id") Long id, @RequestParam("count") Integer count) {
        tStorageService.decreaseStorage(id, count);
        return new CommonResult<>(200, "扣减库存成功");
    }
}

