package com.mm.gemini.base.pojo;

import lombok.Data;

/**
 * 自定义返回结果Vo
 */
@Data
public class ResponseSimpleVO extends ResponseBaseVO {
    private int code;//错误状态码
    private String msg; //错误信息

    public ResponseSimpleVO(String msg,int code) {
        this.code = code;
        this.msg = msg;
    }
}
