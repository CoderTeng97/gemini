package com.mm.gemini.security.service;


import com.mm.gemini.base.pojo.ResponseBaseVO;
import com.mm.gemini.core.model.User;
import org.springframework.http.ResponseEntity;


/**
 * @author ql
 */
public interface SecurityService {
    /**
     * 1、注册 2、已注册的获取token
     *
     * @param email 用户名
     * @param password 密码
     * @return Token
     */
    ResponseBaseVO login(String email,String password);

    /**
     * 刷新token
     *
     * @param token 过期的token
     * @return 新token
     */
    ResponseEntity<ResponseBaseVO> refreshToken(String token);

    ResponseEntity<ResponseBaseVO> register(User user);
}
