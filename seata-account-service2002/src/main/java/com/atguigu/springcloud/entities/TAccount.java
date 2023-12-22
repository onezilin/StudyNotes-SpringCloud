package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 账户表(TAccount)实体类
 *
 * @author onezilin
 * @since 2023-12-22 10:44:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TAccount implements Serializable {
    private static final long serialVersionUID = 120214117227851589L;
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 总额度
     */
    private Double total;
    /**
     * 已用余额
     */
    private Double used;
    /**
     * 剩余可用额度
     */
    private Double residue;

}

