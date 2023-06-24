package com.atguigu.springcloud.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    // 响应状态
    private Integer code;
    // 响应消息
    private String message;
    // 响应数据
    private T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
}
