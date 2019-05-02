package com.mm.gemini.core.pojo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserModifiedDTO {
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

}
