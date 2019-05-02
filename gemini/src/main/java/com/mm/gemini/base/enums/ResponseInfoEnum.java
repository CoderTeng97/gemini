package com.mm.gemini.base.enums;

/**
 * 返回信息枚举
 */
public enum ResponseInfoEnum {
    SYS_ERROR("亲,系统出现小小状况,我们正在抢修中~"),
    FAIL("亲,操作失败，请稍后重试"),
    NOT_FOUND("亲,没有找到你要的信息沃~"),
    OK("操作成功");
    private String msg;

    ResponseInfoEnum(String msg){
        this.msg = msg;
    }

    public String msg(){
        return  this.msg;
    }
}
