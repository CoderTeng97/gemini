package com.mm.gemini.security.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.mm.gemini.base.pojo.*;
import com.mm.gemini.core.model.User;
import com.mm.gemini.security.JwtTokenUtil;
import com.mm.gemini.security.service.SecurityService;
import com.mm.gemini.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author MUMA
 */
@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserService userService;

    @Override
    public ResponseBaseVO login(String email, String password) {
        //以username,password 获取用户信息，判断用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = userService.getOne(
                queryWrapper.eq("email", email)
                        .eq("password", password)
                        .eq("is_del", 0)
        );
        Optional optional = Optional.of(user);
        if (!optional.isPresent()){
            return  new ResponseErrorVO("用户名或密码错误,请重新尝试",400);
        }
        //返回Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("accountType", user.getAccountType());
        claims.put("relationUid", user.getRelationUid());
        String token = jwtTokenUtil.generateToken(claims);
        return new TokenVO(token);
    }

    @Override
    public ResponseEntity<ResponseBaseVO> refreshToken(String token) {
        String newToken = jwtTokenUtil.refreshToken(token);
        if (StringUtils.isEmpty(newToken)) {
            return new ResponseEntity<>(new SimpleErrorVO("旧token无效或者过期超过一天，请重新登录"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new TokenVO(newToken),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBaseVO> register(User user) {
        //TODO 注册
        user.setGmtCreate(LocalDateTime.now());
        if(userService.save(user)){
            //如果为之账户关联主账户
            if (user.getAccountType() != 0 && user.getAccountType()!=null){
                //更新主账户信息
                try {
                    userService.setRelationUid(user.getRelationUid(),user.getId());
                }catch (Exception e){
                    log.error("用户更新关系id 失败" + JSON.toJSONString(e));
                    log.error("用户更新关系id 失败 用户信息" + JSON.toJSONString(user));
                }
            }
        }else {
            return new ResponseEntity<>(new ResponseErrorVO(), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);

    }
}
