package com.mm.gemini.core.pojo.vo;

import lombok.Data;

@Data
public class BaseUserVO {
    private Long id;

    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否为主账户,0为主账户 1为子账户
     */
    private Integer accountType;

    /**
     * 关联用户id
     */
    private Integer uid;
}
