package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 库存(TStorage)实体类
 *
 * @author onezilin
 * @since 2023-12-22 10:56:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TStorage implements Serializable {
    private static final long serialVersionUID = -47329288865473215L;

    private Long id;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 总库存
     */
    private Integer total;
    /**
     * 已用库存
     */
    private Integer used;
    /**
     * 剩余库存
     */
    private Integer residue;

}

