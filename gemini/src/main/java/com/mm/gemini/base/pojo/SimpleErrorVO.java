package com.mm.gemini.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qiulong
 * @date 2019/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SimpleErrorVO extends ResponseBaseVO {
    private static final long serialVersionUID = 1L;
    private String message;

    public SimpleErrorVO() {
        this.message = "服务器内部错误！";
    }

}
