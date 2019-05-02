package com.mm.gemini.core.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mm.gemini.base.controller.BaseController;
import com.mm.gemini.base.enums.ResponseInfoEnum;
import com.mm.gemini.base.pojo.ResponseErrorVO;
import com.mm.gemini.base.pojo.ResponseObjectVO;
import com.mm.gemini.base.pojo.ResponseSuccessVO;
import com.mm.gemini.core.model.User;
import com.mm.gemini.core.pojo.dto.RegisterDTO;
import com.mm.gemini.core.pojo.dto.UserModifiedDTO;
import com.mm.gemini.core.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {
    @Autowired
    UserService userService;

    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return
     */
    @ApiOperation("检查用户名是否存在")
    @GetMapping("/isExistUserName")
    public ResponseEntity isExistUserName(
            String username
    ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        int count = userService.count(
                queryWrapper.eq("username", username.trim())
                        .eq("is_del", 0)
        );
        if (count > 0) {
            return new ResponseEntity<>(new ResponseErrorVO("用户名已存在", 400), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
        }
    }

    /**
     * 检查邮箱是否存在
     *
     * @param email
     * @return
     */
    @ApiOperation("检查用户邮箱是否存在")
    @GetMapping("/isExistUserEmail")
    public ResponseEntity isExistUserEmail(
            String email
    ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        int count = userService.count(
                queryWrapper.eq("email", email.trim())
                        .eq("is_del", 0)
        );
        if (count > 0) {
            return new ResponseEntity<>(new ResponseErrorVO("邮箱已存在", 400), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
        }
    }

    @ApiOperation("用户更新失败")
    @PostMapping("/modified")
    public ResponseEntity modified(
            UserModifiedDTO userModifiedDTO
    ) {
        User user = new User();
        user.setUsername(userModifiedDTO.getUsername());
        user.setBirthday(userModifiedDTO.getBirthday());
        user.setHeadPortrait(userModifiedDTO.getHeadPortrait());
        user.setSex(userModifiedDTO.getSex());
        user.setId(getUid());
        try {
            if (userService.updateById(user)) {
                return ResponseEntity.ok(new ResponseSuccessVO());
            } else {
                return ResponseEntity.ok(new ResponseErrorVO());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户信息更新失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 获取用户基本信息
     *
     * @return
     */
    @ApiOperation("获取当前用户基本信息")
    @GetMapping("/baseInfo")
    public ResponseEntity baseInfo(
    ) {
        try {
            return ResponseEntity.ok(new ResponseObjectVO<>(userService.getUserBaseInfo(getUid())));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取用户基本信息 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 获取用户基本信息
     *
     * @return
     */
    @ApiOperation("获取关联用户基本信息")
    @GetMapping("/conBaseInfo")
    public ResponseEntity conBaseInfo(
    ) {
        try {
            return ResponseEntity.ok(new ResponseObjectVO<>(userService.getUserBaseInfo(getRelationUid())));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取用户基本信息 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 获取注册天数
     *
     * @return
     */
    @ApiOperation("获取用户在线天数")
    @GetMapping("/getOnlineDays")
    public ResponseEntity getOnlineDays(
    ) {
        return ResponseEntity.ok(new ResponseObjectVO<>(userService.getOnlineDays(getUid())));
    }


}
