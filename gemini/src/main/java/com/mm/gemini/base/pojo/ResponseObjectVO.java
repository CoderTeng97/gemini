package com.mm.gemini.base.pojo;

import lombok.Data;


/**
 * 自定义返回对象Vo
 * @param <T>
 */
@Data
public class ResponseObjectVO<T> extends ResponseBaseVO {
    private T vo;//返回对象
    private int code;//错误状态码
    private String msg; //错误信息

    public ResponseObjectVO(T vo) {
        this.vo = vo;
        this.code = 200;
        this.msg = "操作成功!";
    }

    public ResponseObjectVO(T vo, String msg,int code) {
        this.vo = vo;
        this.code =code;
        this.msg = msg;
    }

}
