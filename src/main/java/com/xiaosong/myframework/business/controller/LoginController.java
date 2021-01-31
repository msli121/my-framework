package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.constant.BusinessConstant;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.dto.WeChatLoginDtoEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.response.UserProfileEntity;
import com.xiaosong.myframework.business.service.LoginService;
import com.xiaosong.myframework.business.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */
@RestController
@RequestMapping("/api")
@Log4j2
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    /**
     * 账号密码登录
     * @param user
     * @return
     */
    @PostMapping(value = "/login")
    public ApiResult login(@RequestBody UserEntity user) {
        UserProfileEntity userProfile = loginService.login(user, BusinessConstant.USER_TYPE_PASSWORD);
        return ApiResult.T("", "登录成功", userProfile);
    }

    /**
     * 微信扫码用户登录
     * @param loginDtoEntity
     * @return
     */
    @PostMapping(value = "/login/we-chat")
    public ApiResult loginByWeChat(@RequestBody WeChatLoginDtoEntity loginDtoEntity) {
        UserEntity currentUser = loginService.getUserInfoByWeChat(loginDtoEntity);
        UserProfileEntity userProfile = loginService.login(currentUser, BusinessConstant.USER_TYPE_WE_CHAT);
        return ApiResult.T("", "微信登录成功", userProfile);
    }

    /**
     * 账号密码用户注册接口
     * @param user
     * @return 用户 profile
     */
    @PostMapping(value = "/registry")
    public ApiResult registry(@RequestBody UserEntity user) {
        UserEntity requestUser = new UserEntity();
        BeanUtils.copyProperties(user, requestUser);
        // 注册账号密码新用户
        loginService.passwordRegistry(user);
        // 新用户登录
        UserProfileEntity userProfile = loginService.login(requestUser, BusinessConstant.USER_TYPE_PASSWORD);
        return ApiResult.T("", "注册成功", userProfile);
    }

    /**
     * 用户身份认证
     * @return
     */
    @GetMapping(value = "/authentication")
    public ApiResult authentication(){
        return ApiResult.T("", "身份认证成功");
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    public ApiResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ApiResult.T("", "退出登录");
    }
}
