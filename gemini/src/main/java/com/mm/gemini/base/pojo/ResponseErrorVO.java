package com.mm.gemini.base.pojo;

/**
 * 执行失败返回结果Vo
 */
public class ResponseErrorVO extends ResponseSimpleVO {

    public ResponseErrorVO(String msg,int code) {
        super(msg,code);
    }

    public ResponseErrorVO() {
        super("操作失败!",400);
    }
}
