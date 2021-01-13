package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.entity.ApiResult;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.service.UserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

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
    public ApiResult login(@RequestBody UserEntity requestUser, HttpSession session) {
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

        UserEntity user = userService.getByUsernameAndPassword(username, requestUser.getPassword());
        if (null == user) {
            String message = "账号密码错误";
            System.out.println(message);
            return ApiResult.T(400);
        } else {
            System.out.println("密码正确！");
            session.setAttribute("user", user);
            return ApiResult.T(200);
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
            return ApiResult.F("001", message);
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
        return ApiResult.T();
    }
}
