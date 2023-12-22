package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单表(TOrder)实体类
 *
 * @author onezilin
 * @since 2023-12-22 10:38:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TOrder implements Serializable {
    private static final long serialVersionUID = -19740813518791161L;

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 金额
     */
    private Double money;
    /**
     * 订单状态:  0:创建中 1:已完结
     */
    private Integer status;

}

