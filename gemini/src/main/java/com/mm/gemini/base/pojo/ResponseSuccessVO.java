package com.mm.gemini.base.pojo;

/**
 * 执行返回结果Vo
 */
public class ResponseSuccessVO extends ResponseSimpleVO {

    public ResponseSuccessVO(String msg,int code) {
        super(msg,code);
    }
    public ResponseSuccessVO() {
        super("操作成功!",200 );
    }
}
