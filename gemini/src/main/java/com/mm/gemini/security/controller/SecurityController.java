package com.mm.gemini.security.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mm.gemini.base.controller.BaseController;
import com.mm.gemini.base.enums.ResponseInfoEnum;
import com.mm.gemini.base.pojo.ResponseBaseVO;
import com.mm.gemini.base.pojo.ResponseErrorVO;
import com.mm.gemini.base.pojo.ResponseSuccessVO;
import com.mm.gemini.core.model.User;
import com.mm.gemini.core.pojo.dto.RegisterDTO;
import com.mm.gemini.helper.utils.RandomCodeUtils;
import com.mm.gemini.helper.utils.SendEmailUtils;
import com.mm.gemini.security.service.SecurityService;
import com.mm.gemini.core.service.UserService;
import com.mm.gemini.helper.utils.EncryptUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 安全控制
 */
@Slf4j
@RestController
public class SecurityController extends BaseController {

    @Autowired
    SecurityService securityService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    SendEmailUtils sendEmailUtils;


    private static final String SENDEMAIL_PREFIX = "EMAILCODE";

    private static final String KEY = "m1u2m3a4";

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseEntity login(
            String email,
            String password
    ) {
        try {
            ResponseBaseVO baseVO = securityService.login(email, EncryptUtil.encrypt(password, KEY));
            return new ResponseEntity<>(baseVO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseEntity register(
            RegisterDTO registerDTO
    ) {
        //TODO 校检邮箱验证码是否正确
        if (StringUtils.isEmpty(registerDTO.getAuthCode())) {
            return new ResponseEntity<>(new ResponseErrorVO("邮箱验证码不能为空", 400), HttpStatus.OK);
        }
        System.out.println(SENDEMAIL_PREFIX + registerDTO.getEmail().trim());
        String authCode = (String) redisTemplate.opsForValue().get(SENDEMAIL_PREFIX + registerDTO.getEmail().trim());
        if (!StringUtils.isEmpty(authCode)) {
            if (!registerDTO.getAuthCode().trim().equals(authCode.trim())) {
                return new ResponseEntity<>(new ResponseErrorVO("邮箱验证码错误", 400), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ResponseErrorVO("邮箱验证码错误", 400), HttpStatus.OK);
        }

        User user = new User();
        /**检查邀请码是否正确*/
        if (registerDTO.getAccountType() != 0) {
            //检查邀请码正确
            String relationUidS = (String) redisTemplate.opsForValue().get(registerDTO.getInviteCode().trim());
            if (StringUtils.isEmpty(relationUidS)) {
                return new ResponseEntity<>(new ResponseErrorVO("账户邀请码错误", 400), HttpStatus.OK);
            } else {
                //设置关联用户id值
                user.setRelationUid(Long.valueOf(relationUidS));
            }
        }
        user.setEmail(registerDTO.getEmail());
        user.setAccountType(registerDTO.getAccountType());
        user.setPassword(EncryptUtil.encrypt(registerDTO.getPassword(), KEY));
        user.setUsername(registerDTO.getUsername());
        try {
            ResponseEntity responseEntity = securityService.register(user);
            //注册后成功删除redis key
            //redisTemplate.delete(SENDEMAIL_PREFIX + registerDTO.getEmail());
            return responseEntity;
        } catch (Exception e) {
            log.error("用户注册失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @ApiOperation("发送邮箱验证码")
    @PostMapping(value = "/sendEmailRandomCode")
    public ResponseEntity<ResponseBaseVO> sendEmailRandomCode(String email) {
        String code = (String) redisTemplate.opsForValue().get(SENDEMAIL_PREFIX + email);
        if (!StringUtils.isEmpty(code)) {
            return new ResponseEntity<>(new ResponseErrorVO("亲，您操作太频繁了，稍后再试~", HttpStatus.OK.value()), HttpStatus.OK);
        }
        String randomCode = RandomCodeUtils.getRandomCode();
        StringBuffer buffer = new StringBuffer();
        buffer.append("尊敬的用户欢迎您来到双子博客：" + '\n');
        buffer.append("--------------------------" + '\n');
        buffer.append("注册随机验证码：");
        buffer.append(randomCode);
        try {
            sendEmailUtils.sendVerificationCode(email, buffer.toString(), "双子博客注册验证码提示");
            //邮箱随机码存redis
            redisTemplate.opsForValue().set(SENDEMAIL_PREFIX + email, randomCode, Long.valueOf("60"), TimeUnit.SECONDS);
            return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("邮箱发送失败" + JSON.toJSONString(e));
            return new ResponseEntity<>(new ResponseErrorVO("亲，失败了，稍后再试", HttpStatus.OK.value()), HttpStatus.OK);
        }

    }

    @ApiOperation("检查邮箱验证码是否正确")
    @PostMapping(value = "/verifyEmailRandomCode")
    public ResponseEntity verifyEmailRandomCode(
            @ApiParam("邮箱")
            String email,
            @ApiParam("验证码")
            String rcode
    ) {
        if (!(StringUtils.isEmpty(rcode)&&StringUtils.isEmpty(rcode))){
            return ResponseEntity.ok(new ResponseErrorVO("参数不能为空沃",400));
        }
        String code = (String) redisTemplate.opsForValue().get("VERYEMAILCODE" + getUid());
        if (!StringUtils.isEmpty(code)) {
            return new ResponseEntity<>(new ResponseErrorVO("亲，您操作太频繁了，稍后再试~", HttpStatus.OK.value()), HttpStatus.OK);
        }
        //检查邮箱验证码是否正确
        String authCode = (String) redisTemplate.opsForValue().get(SENDEMAIL_PREFIX + email.trim());
        if (!StringUtils.isEmpty(authCode)) {
            if (rcode.trim().equals(authCode.trim())) {
                return new ResponseEntity<>(new ResponseErrorVO("邮箱验证码错误", 400), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ResponseErrorVO("邮箱验证码错误", 400), HttpStatus.OK);
        }
        return  ResponseEntity.ok(new ResponseSuccessVO());
    }

    @ApiOperation("更新邮箱")
    @PostMapping("/modified/email")
    public ResponseEntity modifiedEmail(
            String email
    ){
        if (StringUtils.isEmpty(email)){
            return ResponseEntity.ok(new ResponseErrorVO("参数不能为空",400));
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        boolean isUpdate = userService.update(
                updateWrapper.eq("id",getUid()).set(true,"email",email)
        );
        if(isUpdate){
            return ResponseEntity.ok(new ResponseSuccessVO());
        }else {
            return ResponseEntity.ok(new ResponseErrorVO());
        }
    }
    @ApiOperation("更新密码")
    @PostMapping("/modified/password")
    public ResponseEntity modifiedPassword(
            String password
    ){
        if (StringUtils.isEmpty(password)){
            return ResponseEntity.ok(new ResponseErrorVO("参数不能为空",400));
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        boolean isUpdate = userService.update(
                updateWrapper.eq("id",getUid()).set(true,"password",EncryptUtil.encrypt(password,KEY))
        );
        if(isUpdate){
            return ResponseEntity.ok(new ResponseSuccessVO());
        }else {
            return ResponseEntity.ok(new ResponseErrorVO());
        }
    }



    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    @ApiOperation("刷新token")
    @PostMapping(value = "token/refresh")
    public ResponseEntity<ResponseBaseVO> refreshToken(
            @ApiParam(value = "过期的token", required = true)
            @RequestParam(name = "token") String token) {
        return securityService.refreshToken(token);
    }
}
