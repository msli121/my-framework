package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.response.UserProfileEntity;
import com.xiaosong.myframework.business.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
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

    @PostMapping(value = "/login")
    public ApiResult login(@RequestBody UserEntity requestUser) {
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setTimeout(10000);
        UsernamePasswordToken token = new UsernamePasswordToken(username, requestUser.getPassword());
        token.setRememberMe(true);
        try {
            subject.login(token);
            UserEntity user = userService.getByUsername(username);
            if(user.getLocked() == 1) {
                return ApiResult.F("400", "该用户已被禁用");
            }
            log.info("用户 [ " + username + " ]" + "登录成功");
            return ApiResult.T("200", "登录成功", new UserProfileEntity(user));
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
        // 随机生成用户头像
        user.setSysHeadIcon(userService.generateHeadIconRandom());
        user.setEnabled((byte) 1);
        user.setLocked((byte) 0);
        userService.add(user);
        return ApiResult.T("200", "注册成功", user);
    }

    @GetMapping(value = "/authentication")
    public ApiResult authentication(){
        return ApiResult.T("200", "身份认证成功");
    }

    @GetMapping("/logout")
    public ApiResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ApiResult.T("200", "退出登录");
    }
}
