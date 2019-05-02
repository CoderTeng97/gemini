package com.mm.gemini.core.pojo.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否为主账户,0为主账户 1为子账户
     */
    private Integer accountType;

    /**
     * 账户邀请码
     */
    private String inviteCode;

    /**
     * 邮箱验证码
     */
    private String authCode;
}
