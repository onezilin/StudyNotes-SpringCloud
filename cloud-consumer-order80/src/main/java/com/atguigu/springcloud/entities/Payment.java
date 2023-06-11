package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Payment)实体类
 *
 * @author onezilin
 * @since 2023-06-04 18:57:25
 * <p>
 * create table payment
 * (
 * id     bigint(20) not null auto_increment comment 'id',
 * serial varchar(200) default '',
 * primary key (id)
 * ) engine = InnoDB
 * auto_increment = 1
 * default charset = utf8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

    private static final long serialVersionUID = 964743913181824020L;
    /**
     * id
     */
    private Long id;

    private String serial;

}

