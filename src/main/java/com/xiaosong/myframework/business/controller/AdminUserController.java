package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {
    @Autowired
    UserService userService;

    /**
     * 获取系统所有用户
     * @return
     */
    @GetMapping("/all")
    public ApiResult getAllUser() {
        return ApiResult.T(userService.listAllUserDto());
    }
}
