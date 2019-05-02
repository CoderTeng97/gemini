package com.mm.gemini.base.controller;


import com.mm.gemini.security.UserInfoDetails;
import com.mm.gemini.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用 BaseController
 */
public class BaseController {


    @Autowired
    UserService userService;

    /**
     * 获取用户ID
     *
     * @return
     */
    public Long getUid() {
        if ("local".equals(getEnv())) {
            return Long.valueOf("1");
        } else {
            UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getId();
        }
    }

    /**
     * 获取关联用户id
     *
     * @return
     */
    public Long getRelationUid() {
        if ("local".equals(getEnv())) {
            return Long.valueOf("2");
        } else {
            UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getRelationId();
        }
    }

    /**
     * 获取邮箱
     *
     * @return
     */
    public String getEmail() {
        if ("local".equals(getEnv())) {
            return "1510545794@qq.com";
        } else {
            UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getEmail();
        }
    }

    public Integer getAccountType() {
        if ("local".equals(getEnv())) {
            return Integer.valueOf("0");
        } else {
            UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getAccountType();
        }
    }

    /**
     * 获取头像
     *
     * @param uid
     * @return
     */
    public String getHeadPortriat(Long uid) {
        if (uid != null) {
            return userService.getHeadPortrait(uid);
        } else {
            return null;
        }
    }

    /**
     * 获取用户关联的多个id(包括自己的id)
     *
     * @return ids 多个id
     */
    public List<Long> getConIds() {
        List<Long> ids = new ArrayList<>();
        if ("local".equals(getEnv())) {
            ids.add(Long.valueOf(1));
            ids.add(Long.valueOf(2));
            return ids;
        }
        ids.add(getUid());
        ids.add(getRelationUid());
        return ids;
    }

    /**
     * 获取环境
     *
     * @return
     */
    public String getEnv() {
        String env = System.getProperty("user.env");
        return env;
    }
}
