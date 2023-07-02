package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Payment)表控制层
 *
 * @author onezilin
 * @since 2023-06-04 19:20:45
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {
    /**
     * 服务对象
     */
    @Resource
    private PaymentService paymentService;

    /**
     * 分页查询
     *
     * @param payment     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Payment>> queryByPage(Payment payment, PageRequest pageRequest) {
        return ResponseEntity.ok(this.paymentService.queryByPage(payment, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public CommonResult<Payment> queryById(@PathVariable("id") Long id) {
        return new CommonResult<>(200, "查询成功", this.paymentService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param payment 实体
     * @return 新增结果
     */
    @PostMapping("/create")
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        return ResponseEntity.ok(this.paymentService.insert(payment));
    }

    /**
     * 编辑数据
     *
     * @param payment 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Payment> edit(Payment payment) {
        return ResponseEntity.ok(this.paymentService.update(payment));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.paymentService.deleteById(id));
    }

}

