package com.mm.gemini.core.pojo.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserBaseInfoVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 头像
     */
    private String headPortrait;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否为主账户,0为主账户 1为子账户
     */
    private Integer accountType;
}
