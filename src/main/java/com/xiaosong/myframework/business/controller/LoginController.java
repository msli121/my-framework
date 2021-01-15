package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.entity.ApiResult;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public ApiResult login(@RequestBody UserEntity requestUser) {
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setTimeout(10000);
        UsernamePasswordToken token = new UsernamePasswordToken(username, requestUser.getPassword());
        token.setRememberMe(true);
        try {
            subject.login(token);
            return ApiResult.T("200", "登录成功");
        } catch (AuthenticationException e) {
            String message = "账号或密码错误";
            return ApiResult.F("400", message);
        }
    }

    @PostMapping(value = "/registry")
    public ApiResult registry(@RequestBody UserEntity user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被使用";
            return ApiResult.F("400", message);
        }
        // 生成盐,默认长度16位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return ApiResult.T("200", "注册成功");
    }

    @GetMapping(value = "/authentication")
    public ApiResult authentication(){
        return ApiResult.T("200", "身份认证成功");
    }

    @GetMapping("/logout")
    public ApiResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ApiResult.T("200", message);
    }
}
