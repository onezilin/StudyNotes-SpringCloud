package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.TAccount;
import com.atguigu.springcloud.service.TAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 账户表(TAccount)表控制层
 *
 * @author onezilin
 * @since 2023-12-22 10:44:32
 */
@RestController
@RequestMapping("/tAccount")
public class TAccountController {
    /**
     * 服务对象
     */
    @Resource
    private TAccountService tAccountService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public CommonResult<TAccount> queryById(@PathVariable("id") Long id) {
        return new CommonResult<>(200, "查询成功", tAccountService.queryById(id));
    }

    /**
     * 扣减账户余额
     *
     * @param id    用户id
     * @param money 扣减金额
     */
    @PostMapping("/decreaseMoney")
    public CommonResult decreaseMoney(@RequestParam("id") Long id, @RequestParam("money") BigDecimal money) {
        tAccountService.decreaseMoney(id, money);
        return new CommonResult<>(200, "扣减账户余额成功！");
    }
}

